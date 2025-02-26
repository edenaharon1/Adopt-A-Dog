package com.example.adoptadog.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.adoptadog.R

class PostFragment : Fragment() {

    private var isLiked = false
    private var likeCount = 584

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post, container, false)

        val likeIcon = view.findViewById<ImageView>(R.id.likeIcon)
        val likeText = view.findViewById<TextView>(R.id.likeCount)
        val commentInput = view.findViewById<EditText>(R.id.commentInput)
        val sendCommentButton = view.findViewById<Button>(R.id.sendCommentButton)
        val commentsContainer = view.findViewById<LinearLayout>(R.id.commentsContainer)

        likeIcon.setOnClickListener {
            isLiked = !isLiked
            likeCount = if (isLiked) likeCount + 1 else likeCount - 1
            likeIcon.setImageResource(if (isLiked) R.drawable.heart_icon else R.drawable.empty_heart)
            likeText.text = likeCount.toString()
        }

        sendCommentButton.setOnClickListener {
            val newComment = commentInput.text.toString().trim()
            if (newComment.isNotEmpty()) {
                val commentView = TextView(requireContext()).apply {
                    text = newComment
                    textSize = 14f
                    setTextColor(resources.getColor(R.color.black))
                }
                commentsContainer.addView(commentView)
                commentInput.text.clear()
            }
        }

        return view
    }
}
