package com.example.adoptadog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

class FragmentProfile : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // קבלת הכפתור editPosts
        val editPostsButton: Button = view.findViewById(R.id.editPostsButton) // ודא שזה ה-ID הנכון של הכפתור שלך.

        // הוספת מאזין לחיצה
        editPostsButton.setOnClickListener {
            // ניווט לפרגמנט עריכת הפוסטים דרך ה-NavController וה-Action שיצרת ב-NavGraph
            findNavController().navigate(R.id.action_profile_to_edit_posts) // ודא שזה ה-ID של ה-Action שלך ב-NavGraph
        }
    }
}
