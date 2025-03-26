package com.example.adoptadog

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        progressBar = findViewById(R.id.progressBar)
        auth = Firebase.auth
        emailEditText = findViewById(R.id.emailInput)
        passwordEditText = findViewById(R.id.passwordInput)

        val loginButton = findViewById<Button>(R.id.loginButton)
        val signInButton = findViewById<Button>(R.id.sign_in)

        loginButton.setOnClickListener {
            loginUser()
        }

        signInButton.setOnClickListener {
            // פתיחת NavHostActivity עם פרגמנט ההרשמה
            val intent = Intent(this, NavHostActivity::class.java)
            intent.putExtra("openSignUpFragment", true)
            startActivity(intent)
            finish() // סגירת LoginActivity
        }
    }

    private fun loginUser() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            startLoading()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        navigateToNavHostActivity(false)
                    } else {
                        Toast.makeText(baseContext, "התחברות נכשלה. אנא נסה שוב.", Toast.LENGTH_SHORT).show()
                        stopLoading()
                    }
                }
        } else {
            Toast.makeText(this, "אנא מלא את פרטי ההתחברות", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signUpUser() {
        // פונקציה זו לא נקראת יותר ישירות מלחצן ה-SignUp
        // הלוגיקה של הרשמה נעשת בפרגמנט ההרשמה
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
                    Toast.makeText(baseContext, "נתוני משתמש נשמרו בהצלחה.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(baseContext, "שמירת נתוני משתמש נכשלה: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun navigateToNavHostActivity(openSignUp: Boolean) {
        val intent = Intent(this, NavHostActivity::class.java)
        intent.putExtra("openSignUpFragment", openSignUp)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
            finish()
        }, 1500)
    }

    private fun startLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        progressBar.visibility = View.GONE
    }
}