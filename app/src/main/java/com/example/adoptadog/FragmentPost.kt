package com.example.adoptadog.fragments

import android.os.Bundle
import android.util.Log
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
import com.example.adoptadog.MyApplication
import com.example.adoptadog.R
import com.example.adoptadog.database.Post
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostFragment : Fragment() {

    private lateinit var postImage: ImageView
    private lateinit var postDescription: TextView
    private lateinit var commentsTextView: TextView
    private lateinit var addCommentEditText: EditText
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
        commentsTextView = view.findViewById(R.id.commentsTextView)
        addCommentEditText = view.findViewById(R.id.addCommentEditText)
        adoptNowButton = view.findViewById(R.id.adoptNowButton)
        backButton = view.findViewById(R.id.backButton)

        postId = arguments?.getLong("postId") ?: -1

        if (postId == -1L) {
            Toast.makeText(requireContext(), "Post ID not found", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        loadPostData()

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

            val post = postDao.getPostById(postId)

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
        commentsTextView.text = "Comments: ${currentPost.comments.size}"

        // TODO: Display comments in a RecyclerView or ListView
    }
}