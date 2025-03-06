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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adoptadog.LoginActivity
import com.example.adoptadog.MyApplication
import com.example.adoptadog.R
import com.example.adoptadog.database.Post
import kotlinx.coroutines.launch

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

        // אתחול ה-NavController
        val navController = findNavController()

        // חיבור הכפתורים מה-XML
        val profileIcon = view.findViewById<ImageView>(R.id.profileIcon)
        val logoutButton: Button = view.findViewById(R.id.logoutButton)

        // הגדרת הלחיצה על האייקון - ניווט לפרגמנט פרופיל
        profileIcon.setOnClickListener {
            navController.navigate(R.id.FragmentProfile)
        }

        // יציאה מהחשבון בלחיצה על כפתור "Log Out"
        logoutButton.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        recyclerView = view.findViewById(R.id.recyclerViewPosts)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2) // שינוי ל-GridLayoutManager ו-requireContext()

        // אתחול האדפטר עם למדה ריקה
        adapter = PostAdapter(mutableListOf()) { }

        recyclerView.adapter = adapter

        val database = (requireActivity().application as MyApplication).database
        val postDao = database.postDao()

        lifecycleScope.launch {
            postDao.insert(Post(title = "My Post 1", content = "This is my post content 1."))
            postDao.insert(Post(title = "My Post 2", content = "This is my post content 2."))
            postDao.insert(Post(title = "My Post 3", content = "This is my post content 3."))
            postDao.insert(Post(title = "My Post 4", content = "This is my post content 4."))
            val posts = postDao.getAllPosts()
            adapter.updatePosts(posts)
        }
    }
}