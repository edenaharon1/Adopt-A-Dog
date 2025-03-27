package com.example.adoptadog.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adoptadog.MyApplication
import com.example.adoptadog.R
import com.example.adoptadog.database.Comment
import com.example.adoptadog.database.Post
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostFragment : Fragment() {

    private lateinit var postImage: ImageView
    private lateinit var postDescription: TextView
    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var addCommentEditText: EditText
    private lateinit var addCommentButton: Button
    private lateinit var adoptNowButton: Button
    private lateinit var backButton: Button
    private lateinit var currentPost: Post

    private var postId: Long = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postImage = view.findViewById(R.id.postImage)
        postDescription = view.findViewById(R.id.postDescription)
        commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView)
        addCommentEditText = view.findViewById(R.id.addCommentEditText)
        addCommentButton = view.findViewById(R.id.addCommentButton)
        adoptNowButton = view.findViewById(R.id.adoptNowButton)
        backButton = view.findViewById(R.id.backButton)

        postId = arguments?.getLong("postId") ?: -1

        if (postId == -1L) {
            Toast.makeText(requireContext(), "Post ID not found", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        loadPostData()

        addCommentButton.setOnClickListener {
            addComment()
        }

        backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        adoptNowButton.setOnClickListener {
            // Add logic for adopting the dog
        }
    }

    private fun loadPostData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val database = (requireActivity().application as MyApplication).database
            val postDao = database.postDao()

            val post = postDao.getPostById(postId.toString())

            withContext(Dispatchers.Main) {
                if (post != null) {
                    currentPost = post
                    updateUI()
                } else {
                    Toast.makeText(requireContext(), "Post not found", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun updateUI() {
        Picasso.get()
            .load(currentPost.imageUrl)
            .fit()
            .centerCrop()
            .into(postImage)

        postDescription.text = currentPost.description

        commentsAdapter = CommentsAdapter(currentPost.comments)
        commentsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = commentsAdapter
        }
    }



    private fun addComment() {
        val commentText = addCommentEditText.text.toString().trim()
        if (commentText.isNotEmpty()) {
            lifecycleScope.launch(Dispatchers.IO) {
                val database = (requireActivity().application as MyApplication).database
                val postDao = database.postDao()
                val currentUser = FirebaseAuth.getInstance().currentUser

                if (currentUser != null) {
                    val authorId = currentUser.displayName ?: "user123" // או ערך ברירת מחדל אחר

                    val comment = Comment(
                        authorId = authorId,
                        text = commentText,
                        postId = currentPost.id
                    )

                    currentPost.comments.add(comment)
                    postDao.updatePost(currentPost)

                    withContext(Dispatchers.Main) {
                        addCommentEditText.text.clear()
                        commentsAdapter.notifyDataSetChanged()
                        updateUI()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Please log in to comment.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

