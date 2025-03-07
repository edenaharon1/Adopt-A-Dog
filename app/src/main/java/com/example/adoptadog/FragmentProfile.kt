package com.example.adoptadog.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.adoptadog.NavHostActivity
import com.example.adoptadog.R

class FragmentProfile : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✨ מציאת הכפתורים
        val editPostsButton: Button = view.findViewById(R.id.editPostsButton)
        val returnToHP: Button = view.findViewById(R.id.backToHomeButton)
        val uploadPostButton = view.findViewById<Button>(R.id.uploadPostButton)

        // ✅ מעבר להעלאת פוסט
        uploadPostButton.setOnClickListener {
            (activity as? NavHostActivity)?.startLoading()
            findNavController().navigate(R.id.action_FragmentProfile_to_uploadPostFragment)
        }

        // ✅ מעבר לעריכת פוסטים
        editPostsButton.setOnClickListener {
            (activity as? NavHostActivity)?.startLoading()
            findNavController().navigate(R.id.action_profile_to_edit_posts)
        }

        // ✅ חזרה לעמוד הבית
        returnToHP.setOnClickListener {
            (activity as? NavHostActivity)?.startLoading()
            findNavController().navigate(R.id.action_FragmentProfile_to_homePageFragment)
        }
        (activity as? NavHostActivity)?.stopLoading()
    }
}