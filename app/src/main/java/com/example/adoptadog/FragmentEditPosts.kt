package com.example.adoptadog.fragments

import PostAdapter
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adoptadog.NavHostActivity
import com.example.adoptadog.R
import com.example.adoptadog.database.AppDatabase
import com.example.adoptadog.database.Post
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditPostsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postsAdapter: PostAdapter
    private var selectedImageUri: Uri? = null
    private var currentPost: Post? = null
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? NavHostActivity)?.startLoading()

        recyclerView = view.findViewById(R.id.recyclerViewPosts)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        postsAdapter = PostAdapter(mutableListOf(), findNavController(), isEditMode = true, onItemClick = { post ->
            currentPost = post
            showPostDialog(post)
        })

        recyclerView.adapter = postsAdapter

        val exitButton: Button = view.findViewById(R.id.exit)

        exitButton.setOnClickListener {
            findNavController().navigate(R.id.action_editPostsFragment_to_FragmentProfile)
        }

        loadUserPosts()
    }

    private fun loadUserPosts() {
        lifecycleScope.launch(Dispatchers.IO) {
            val userId = getLoggedInUserId()
            Log.d("EditPostsFragment", "User ID: $userId")
            val posts = AppDatabase.getDatabase(requireContext(), lifecycleScope).postDao().getPostsByUserId(userId)
            Log.d("EditPostsFragment", "Number of posts: ${posts.size}")
            withContext(Dispatchers.Main) {
                postsAdapter.updatePosts(posts.toMutableList())
                (activity as? NavHostActivity)?.stopLoading()
            }
        }
    }

    private fun getLoggedInUserId(): String {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "defaultUserId"
        Log.d("EditPostsFragment", "User ID from Firebase: $userId")
        return userId
    }

    private fun showPostDialog(post: Post) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.edit_post_dialog, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)

        val imageView = dialogView.findViewById<ImageView>(R.id.editPostImage)
        val editText = dialogView.findViewById<EditText>(R.id.editPostText)
        val saveButton = dialogView.findViewById<Button>(R.id.saveButton)
        val changeImageButton = dialogView.findViewById<Button>(R.id.changeImageButton)
        val deleteButton = dialogView.findViewById<Button>(R.id.deleteButton)

        Glide.with(this)
            .load(Uri.parse(post.imageUrl))
            .into(imageView)

        editText.setText(post.description)

        val dialog = builder.create()
        dialog.show()

        changeImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        saveButton.setOnClickListener {
            val newDescription = editText.text.toString()
            updatePostInDatabase(post.id.toString(), newDescription, selectedImageUri)
            dialog.dismiss()
        }

        deleteButton.setOnClickListener {
            deletePostFromDatabase(post)
            dialog.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            val imageView = view?.findViewById<ImageView>(R.id.editPostImage)
            imageView?.setImageURI(selectedImageUri)
        }
    }

    private fun updatePostInDatabase(postId: String, newDescription: String, imageUri: Uri?) {
        lifecycleScope.launch(Dispatchers.IO) {
            val postDao = AppDatabase.getDatabase(requireContext(), lifecycleScope).postDao()
            val existingPost = postDao.getPostById(postId)
            if (existingPost != null) {
                val updatedPost = existingPost.copy(description = newDescription, imageUrl = imageUri?.toString() ?: existingPost.imageUrl)
                postDao.updatePost(updatedPost)
                loadUserPosts()
            }
        }
    }

    private fun deletePostFromDatabase(post: Post) {
        lifecycleScope.launch(Dispatchers.IO) {
            val postDao = AppDatabase.getDatabase(requireContext(), lifecycleScope).postDao()
            postDao.delete(post)
            loadUserPosts()
        }
    }
}