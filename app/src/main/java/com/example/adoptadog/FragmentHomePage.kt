package com.example.adoptadog.fragments

import PostAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adoptadog.LoginActivity
import com.example.adoptadog.MyApplication
import com.example.adoptadog.NavHostActivity
import com.example.adoptadog.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomePageFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter

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


        recyclerView = view.findViewById(R.id.postsRecyclerView) // שימוש ב-postsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // שימוש ב-LinearLayoutManager

        val database = (requireActivity().application as MyApplication).database
        val postDao = database.postDao()

        adapter = PostAdapter(mutableListOf()) { post ->
            // כאן תוסיף את הקוד שיעביר לפרגמנט אחר
            // למשל, navController.navigate(R.id.action_homePageFragment_to_postDetailsFragment)
        }

        recyclerView.adapter = adapter

        loadPosts(postDao)
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