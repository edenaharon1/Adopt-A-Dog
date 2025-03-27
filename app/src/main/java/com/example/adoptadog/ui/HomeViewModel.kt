package com.example.adoptadog.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adoptadog.database.AppDatabase
import com.example.adoptadog.database.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val database: AppDatabase) : ViewModel() {

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loadedPosts = database.postDao().getAllPosts()

                // הוסיפי לוג עם מספר הפוסטים
                Log.d("HomeViewModel", "Loaded posts: ${loadedPosts.size}")

                // הדפס את פרטי הפוסטים
                loadedPosts.forEachIndexed { index, post ->
                    Log.d("HomeViewModel", "Post $index: ID=${post.id}, Description=${post.description}")
                }

                withContext(Dispatchers.Main) {
                    _posts.value = loadedPosts
                }
            } catch (e: Exception) {
                // הדפסת השגיאה המלאה
                Log.e("HomeViewModel", "Error loading posts", e)
                e.printStackTrace()
            }
        }
    }
}