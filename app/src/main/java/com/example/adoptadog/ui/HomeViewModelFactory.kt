package com.example.adoptadog.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.adoptadog.database.AppDatabase

class HomeViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}