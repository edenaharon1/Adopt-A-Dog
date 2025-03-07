package com.example.adoptadog.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.adoptadog.LoginActivity // הוספת import
import com.example.adoptadog.R

class FragmentProfile : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editPostsButton: Button = view.findViewById(R.id.editPostsButton)
        val returnToHP: Button = view.findViewById(R.id.backToHomeButton)
        val uploadPostButton = view.findViewById<Button>(R.id.uploadPostButton)
        val logoutButton = view.findViewById<Button>(R.id.logoutButton) // הוספת מציאת כפתור ה-Logout

        uploadPostButton.setOnClickListener {
            findNavController().navigate(R.id.action_FragmentProfile_to_uploadPostFragment)
        }

        editPostsButton.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_profile_to_edit_posts)
        }

        returnToHP.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_FragmentProfile_to_homePageFragment)
        }

        // הוספת Listener לכפתור ה-Logout
        logoutButton.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish() // סגירת ה-Activity הנוכחי
        }
    }
}