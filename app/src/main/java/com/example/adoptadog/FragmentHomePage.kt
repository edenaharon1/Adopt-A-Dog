package com.example.adoptadog.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.adoptadog.LoginActivity
import com.example.adoptadog.R

class HomePageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // אתחול ה-NavController
        val navController = findNavController()

        // חיבור הכפתורים מה-XML
        val profileIcon = view.findViewById<ImageView>(R.id.profileIcon)
        val logoutButton: Button = view.findViewById(R.id.logoutButton)

        // הגדרת הלחיצה על האייקון - ניווט לפרגמנט פרופיל
        profileIcon.setOnClickListener {
            // ניווט לפרגמנט פרופיל דרך ה-NavController
            navController.navigate(R.id.FragmentProfile) // מעבר מ-HomePage ל-Profile
        }

        // יציאה מהחשבון בלחיצה על כפתור "Log Out"
        logoutButton.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish() // מסיים את האקטיביטי כדי למנוע חזרה אחורה
        }
    }
}