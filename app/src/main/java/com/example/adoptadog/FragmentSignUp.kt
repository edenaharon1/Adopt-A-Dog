package com.example.adoptadog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import androidx.navigation.fragment.findNavController

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentSignUp : Fragment() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var nameInput: EditText
    private lateinit var signUpButton: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // אתחול השדות מה-XML
        emailInput = view.findViewById(R.id.emailInput)
        passwordInput = view.findViewById(R.id.passwordInput)
        nameInput = view.findViewById(R.id.nameInput)
        signUpButton = view.findViewById(R.id.loginButton)

        // לחצן רישום
        signUpButton.setOnClickListener {
            signUpUser()
        }
    }

    private fun signUpUser() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()
        val name = nameInput.text.toString().trim()

        // בדיקה אם השדות לא ריקים
        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // בדיקה אם האימייל כבר בשימוש
        mAuth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result
                    val existingMethods = result?.signInMethods

                    if (existingMethods != null && existingMethods.isNotEmpty()) {
                        // האימייל כבר בשימוש
                        Toast.makeText(requireContext(), "This email is already in use", Toast.LENGTH_SHORT).show()
                    } else {
                        // האימייל לא בשימוש, ניתן להירשם
                        createUserWithEmailAndPassword(email, password, name)
                    }
                } else {
                    // שגיאה בעת ביצוע הבדיקה
                    Toast.makeText(requireContext(), "Error checking email: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun createUserWithEmailAndPassword(email: String, password: String, name: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // הרישום הצליח
                    val user = mAuth.currentUser

                    user?.let {
                        // שמירת שם המשתמש ב-Firebase Realtime Database
                        val userId = user.uid
                        mDatabase.child("users").child(userId).child("name").setValue(name)
                            .addOnCompleteListener { task1 ->
                                if (task1.isSuccessful) {
                                    // רישום הצליח
                                    Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show()

                                        //כאן מתבצע המעבר אחרי שהרישום הצליח, ה3 שורות האלה אמורות לבצע מעבר ולא עובדות
                                    val navController = findNavController()
                                    navController.popBackStack() // מנקה את הסטאק
                                    navController.navigate(R.id.action_fragmentSignUp_to_homePageFragment)

                                } else {
                                    // שגיאה בשמירה ב-Database
                                    Toast.makeText(requireContext(), "Error saving user data", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    // הרישום נכשל
                    Toast.makeText(requireContext(), "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentSignUp().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

