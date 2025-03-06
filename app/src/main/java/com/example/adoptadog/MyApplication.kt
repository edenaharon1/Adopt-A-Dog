package com.example.adoptadog

import android.app.Application
import androidx.room.Room
import com.example.adoptadog.database.AppDatabase

class MyApplication : Application() {
    val database by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "app_database")
            .build()
    }
}