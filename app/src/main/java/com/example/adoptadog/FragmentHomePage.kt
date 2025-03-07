package com.example.adoptadog.fragments

import PostAdapter
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager // שינוי כאן
import androidx.recyclerview.widget.RecyclerView
import com.example.adoptadog.LoginActivity
import com.example.adoptadog.MyApplication
import com.example.adoptadog.NavHostActivity
import com.example.adoptadog.R
import com.example.adoptadog.ui.HomeViewModel
import com.example.adoptadog.ui.HomeViewModelFactory

class HomePageFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter
    private lateinit var viewModel: HomeViewModel

    private val READ_MEDIA_IMAGES_REQUEST_CODE = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        val profileIcon = view.findViewById<ImageView>(R.id.profileIcon)
        val logoutButton: Button = view.findViewById(R.id.logoutButton)
        val addPostButton: Button = view.findViewById(R.id.addPostButton)


        profileIcon.setOnClickListener {
            (activity as? NavHostActivity)?.startLoading()
            navController.navigate(R.id.FragmentProfile)
        }

        logoutButton.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        addPostButton.setOnClickListener {
            (activity as? NavHostActivity)?.startLoading()
            navController.navigate(R.id.action_homePageFragment_to_uploadPostFragment)
        }

recyclerView = view.findViewById(R.id.postsRecyclerView);
recyclerView.layoutManager = new GridLayoutManager(requireContext(), 2); // שימוש ב-GridLayoutManager עם 2 עמודות

val database = (requireActivity().application as MyApplication).database;
val postDao = database.postDao();

        adapter = PostAdapter(mutableListOf()) { post ->
            // כאן תוסיף את הקוד שיעביר לפרגמנט אחר
            // למשל, navController.navigate(R.id.action_homePageFragment_to_postDetailsFragment)
        }

        recyclerView.adapter = adapter

        // יצירת ViewModel
        val database = (requireActivity().application as MyApplication).database
        val factory = HomeViewModelFactory(database)
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        // צפייה ב-LiveData
        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            adapter.updatePosts(posts)
        }
    }

    override fun onResume() {
        super.onResume()
        checkPermissions()
        requireActivity().invalidateOptionsMenu()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_MEDIA_IMAGES), READ_MEDIA_IMAGES_REQUEST_CODE)
            } else {
                Log.d("HomePageFragment", "READ_MEDIA_IMAGES permission already granted")
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_MEDIA_IMAGES_REQUEST_CODE)
            } else {
                Log.d("HomePageFragment", "READ_EXTERNAL_STORAGE permission already granted")
            }
        }
    }

override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_MEDIA_IMAGES_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("HomePageFragment", "Permissions granted")
                // לאחר קבלת הרשאות, טען את הפוסטים
                val database = (requireActivity().application as MyApplication).database
                val postDao = database.postDao()
                loadPosts(postDao)
            } else {
                Log.d("HomePageFragment", "Permissions denied")
                Toast.makeText(requireContext(), "Permissions denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadPosts(postDao: com.example.adoptadog.database.PostDao) {
        lifecycleScope.launch(Dispatchers.IO) {
            val posts = postDao.getAllPosts()
            withContext(Dispatchers.Main) {
                adapter.updatePosts(posts)
                (activity as? NavHostActivity)?.stopLoading() // עצור ספינר לאחר טעינת פוסטים
            }
        }
    }
        }
    }
}