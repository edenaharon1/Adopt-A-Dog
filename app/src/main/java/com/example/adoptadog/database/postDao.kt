package com.example.adoptadog.database
import androidx.room.*
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post)

    @Query("SELECT * FROM posts")
    suspend fun getAllPosts(): List<Post>

    @Delete
    suspend fun delete(post: Post)
}