package com.example.adoptadog.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post)

    @Delete
    suspend fun delete(post: Post)

    @Query("UPDATE posts SET likes = :likes WHERE id = :postId")
    suspend fun updateLikes(postId: Long, likes: List<String>)

    @Query("UPDATE posts SET comments = :comments WHERE id = :postId")
    suspend fun updateComments(postId: Long, comments: List<Comment>)

    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    suspend fun getAllPosts(): List<Post>
}