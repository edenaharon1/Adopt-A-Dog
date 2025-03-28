package com.example.adoptadog.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post)

    @Delete
    suspend fun delete(post: Post)

    @Query("UPDATE posts SET likes = :likes WHERE id = :postId")
    suspend fun updateLikes(postId: String, likes: List<String>)

    @Query("UPDATE posts SET comments = :comments WHERE id = :postId")
    suspend fun updateComments(postId: String, comments: List<Comment>)

    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    suspend fun getAllPosts(): List<Post>

    @Query("SELECT * FROM posts WHERE id = :postId")
    suspend fun getPostById(postId: String): Post?

    @Update
    suspend fun updatePost(post: Post)

    @Query("DELETE FROM posts")
    suspend fun deleteAllPosts()

    @Query("SELECT * FROM posts WHERE userId = :userId ORDER BY timestamp DESC")
    suspend fun getPostsByUserId(userId: String): List<Post>

    @Query("UPDATE posts SET description = :newDescription WHERE id = :postId")
    suspend fun updatePostDescription(postId: String, newDescription: String)

    @Query("UPDATE posts SET imageUrl = :newImageUrl WHERE id = :postId")
    suspend fun updatePostImage(postId: String, newImageUrl: String)
}