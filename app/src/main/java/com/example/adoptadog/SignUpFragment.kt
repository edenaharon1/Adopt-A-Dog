package com.example.adoptadog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import android.util.Patterns

class SignUpFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        // אתחול FirebaseAuth
        mAuth = FirebaseAuth.getInstance()

        // קבלת הפניות לשדות קלט ולכפתור
        val emailInput = view.findViewById<EditText>(R.id.emailInput)
        val passwordInput = view.findViewById<EditText>(R.id.passwordInput)
        val nameInput = view.findViewById<EditText>(R.id.nameInput)
        val signUpButton = view.findViewById<Button>(R.id.loginButton)

        // פעולת לחיצה על כפתור Sign Up
        signUpButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val name = nameInput.text.toString().trim()

            // בדיקת אם כל השדות מלאים
            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(activity, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // אימות אימייל
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(activity, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // אימות סיסמה (הסיסמה חייבת להיות לפחות 6 תווים)
            if (password.length < 6) {
                Toast.makeText(activity, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // יצירת משתמש חדש ב-Firebase
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // משתמש נרשם בהצלחה
                        val user = mAuth.currentUser
                        user?.let {
                            // עדכון שם משתמש
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build()

                            it.updateProfile(profileUpdates)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        // הפניית המשתמש לדף הבית
                                        startActivity(Intent(activity, homePageActivity::class.java))
                                        activity?.finish()
                                    }
                                }
                        }
                    } else {
                        // במקרה של שגיאה בהרשמה
                        Toast.makeText(activity, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        return view
    }
}
