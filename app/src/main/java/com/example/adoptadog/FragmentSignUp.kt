package com.example.adoptadog

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.adoptadog.database.User
import com.example.adoptadog.database.UserDao
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentSignUp : Fragment(R.layout.fragment_sign_up) {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var signupButton: Button
    private lateinit var userDao: UserDao

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // אתחול רכיבים
        auth = Firebase.auth
        emailEditText = view.findViewById(R.id.emailInput)
        passwordEditText = view.findViewById(R.id.passwordInput)
        nameEditText = view.findViewById(R.id.nameInput)
        signupButton = view.findViewById(R.id.signupButton)
        userDao = (requireActivity().application as MyApplication).database.userDao()

        // טוען רגעי
        (activity as? NavHostActivity)?.startLoading()
        Handler(Looper.getMainLooper()).postDelayed({
            (activity as? NavHostActivity)?.stopLoading()
        }, 1000)

        // מעבר לעמוד התחברות
        view.findViewById<TextView>(R.id.loginButtonLink).setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        // לחיצה על כפתור הרשמה
        signupButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val name = nameEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                (activity as? NavHostActivity)?.startLoading()

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        (activity as? NavHostActivity)?.stopLoading()

                        if (task.isSuccessful) {
                            val firebaseUser = auth.currentUser
                            firebaseUser?.let { user ->
                                val newUser = User(
                                    uid = user.uid,
                                    name = name,
                                    email = email
                                )

                                lifecycleScope.launch(Dispatchers.IO) {
                                    userDao.insert(newUser)

                                    val db = Firebase.firestore
                                    val userData = hashMapOf(
                                        "name" to name,
                                        "email" to email
                                    )
                                    db.collection("users").document(user.uid).set(userData)

                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(requireContext(), "ההרשמה הצליחה!", Toast.LENGTH_SHORT).show()
                                        findNavController().navigate(R.id.action_fragmentSignUp_to_homePageFragment)
                                    }
                                }
                            }
                        } else {
                            when (task.exception) {
                                is FirebaseAuthWeakPasswordException ->
                                    Toast.makeText(context, "סיסמה חלשה. לפחות 6 תווים.", Toast.LENGTH_SHORT).show()

                                is FirebaseAuthInvalidCredentialsException ->
                                    Toast.makeText(context, "אימייל לא תקין.", Toast.LENGTH_SHORT).show()

                                is FirebaseAuthUserCollisionException ->
                                    Toast.makeText(context, "משתמש עם האימייל הזה כבר קיים.", Toast.LENGTH_SHORT).show()

                                else ->
                                    Toast.makeText(context, "הרשמה נכשלה. נסה שוב.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            } else {
                Toast.makeText(context, "אנא מלא את כל השדות", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
