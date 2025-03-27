package com.example.adoptadog.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.adoptadog.LoginActivity
import com.example.adoptadog.NavHostActivity
import com.example.adoptadog.R

class FragmentProfile : Fragment(R.layout.fragment_profile) {

    private lateinit var userNameText: TextView
    private lateinit var profileImageView: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✨ מציאת הכפתורים

        val editPostsButton: Button = view.findViewById(R.id.myPostsButton)
        val returnToHP: Button = view.findViewById(R.id.backToHomeButton)
        val uploadPostButton = view.findViewById<Button>(R.id.uploadPostButton)
        val logoutButton = view.findViewById<Button>(R.id.logoutButton)
        val editProfileButton = view.findViewById<Button>(R.id.editProfileButton)
        userNameText = view.findViewById<TextView>(R.id.userNameText)
        profileImageView = view.findViewById(R.id.profileImage)

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

        logoutButton.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
            (activity as? NavHostActivity)?.stopLoading() // הוספת עצירת ספינר טעינה
        }

        editProfileButton.setOnClickListener {
            findNavController().navigate(R.id.action_FragmentProfile_to_fragmentEditProfile)
        }

        // קבלת שם המשתמש החדש מ-savedStateHandle
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("username")?.observe(viewLifecycleOwner) { username ->
            userNameText.text = "Hello, $username!"
            Log.d("ProfileFragment", "Username received: $username")
        }

        // קבלת תמונת הפרופיל החדשה מ-savedStateHandle
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Uri>("imageUri")?.observe(viewLifecycleOwner) { imageUri ->
            if (imageUri != null) {
                profileImageView.setImageURI(imageUri)
                Log.d("ProfileFragment", "ImageUri received: $imageUri")
            }
        }
    }
}