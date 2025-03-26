package com.example.adoptadog

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FragmentSignUp : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        emailEditText = view.findViewById(R.id.emailInput)
        passwordEditText = view.findViewById(R.id.passwordInput)

        (activity as? NavHostActivity)?.startLoading()

        Handler(Looper.getMainLooper()).postDelayed({
            (activity as? NavHostActivity)?.stopLoading()
        }, 1000)

        val navController = findNavController()

        view.findViewById<TextView>(R.id.loginButtonLink).setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        view.findViewById<Button>(R.id.signupButton).setOnClickListener {
            signUpUser()
        }
    }

    private fun signUpUser() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            (activity as? NavHostActivity)?.startLoading()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    (activity as? NavHostActivity)?.stopLoading()
                    if (task.isSuccessful) {
                        saveUserData()
                        findNavController().navigate(R.id.action_fragmentSignUp_to_homePageFragment)
                    } else {
                        // טיפול בשגיאות ספציפיות
                        when (task.exception) {
                            is FirebaseAuthWeakPasswordException -> {
                                Toast.makeText(context, "סיסמה חלשה. סיסמה צריכה להכיל לפחות 6 תווים.", Toast.LENGTH_SHORT).show()
                            }
                            is FirebaseAuthInvalidCredentialsException -> {
                                Toast.makeText(context, "אימייל לא תקין.", Toast.LENGTH_SHORT).show()
                            }
                            is FirebaseAuthUserCollisionException -> {
                                Toast.makeText(context, "משתמש עם אימייל זה כבר קיים.", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                Toast.makeText(context, "הרשמה נכשלה. אנא נסה שוב.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
        } else {
            Toast.makeText(context, "אנא מלא את פרטי ההרשמה", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveUserData() {
        val user = auth.currentUser
        user?.let {
            val db = Firebase.firestore
            val userData = hashMapOf(
                "email" to it.email,
                // הוסף כאן נתונים נוספים שברצונך לשמור
            )

            db.collection("users").document(it.uid)
                .set(userData)
                .addOnSuccessListener {
                    Toast.makeText(context, "נתוני משתמש נשמרו בהצלחה.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "שמירת נתוני משתמש נכשלה: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}