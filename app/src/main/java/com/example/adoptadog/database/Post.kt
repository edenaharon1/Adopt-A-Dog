package com.example.adoptadog.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val imageUrl: String,
    val userId: String,
    val description: String,
    val likes: List<String>,
    val comments: List<Comment>,
    val timestamp: Long // הוספת שדה timestamp
)

data class Comment(
    val authorId: String,
    val text: String
)