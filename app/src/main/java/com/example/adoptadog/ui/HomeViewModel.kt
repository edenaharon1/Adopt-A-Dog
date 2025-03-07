package com.example.adoptadog.ui

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

    private fun loadPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loadedPosts = database.postDao().getAllPosts()
                withContext(Dispatchers.Main) {
                    _posts.value = loadedPosts
                }
            } catch (e: Exception) {
                // טיפול בשגיאה
                e.printStackTrace()
            }
        }
    }

}