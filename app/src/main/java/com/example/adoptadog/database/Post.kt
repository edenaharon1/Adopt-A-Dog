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
    val comments: MutableList<Comment>, // שינוי כאן
    val timestamp: Long
)

data class Comment(
    val authorId: String,
    val text: String,
    val postId: Long // הוספת שדה postId
)