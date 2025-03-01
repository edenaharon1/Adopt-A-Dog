package com.example.adoptadog.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.adoptadog.R

class FragmentProfile : Fragment(R.layout.fragment_profile) { // וודא שזה שם הפרגמנט שלך

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // מציאת הכפתור
        val editPostsButton: Button = view.findViewById(R.id.editPostsButton)
        val returnToHP: Button = view.findViewById(R.id.backToHomeButton)

        // הגדרת הלחיצה
        editPostsButton.setOnClickListener {
            // קבלת ה-NavController
            val navController = findNavController()

            // ביצוע המעבר באמצעות ה-Action שהגדרת ב-NavGraph
            navController.navigate(R.id.action_profile_to_edit_posts)
        }

        returnToHP.setOnClickListener {
            // קבלת ה-NavController
            val navController = findNavController()

            // ביצוע המעבר באמצעות ה-Action שהגדרת ב-NavGraph
            navController.navigate(R.id.action_FragmentProfile_to_homePageFragment)
        }
    }
}