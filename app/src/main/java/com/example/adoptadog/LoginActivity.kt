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
import androidx.lifecycle.lifecycleScope
import com.example.adoptadog.database.UserDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var userDao: UserDao
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        progressBar = findViewById(R.id.progressBar)
        emailEditText = findViewById(R.id.emailInput)
        passwordEditText = findViewById(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signInButton = findViewById<Button>(R.id.sign_in)

        auth = Firebase.auth
        userDao = (application as MyApplication).database.userDao()

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotBlank() && password.isNotBlank()) {
                startLoading()

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val firebaseUser = auth.currentUser
                            if (firebaseUser != null) {
                                lifecycleScope.launch(Dispatchers.IO) {
                                    val userInRoom = userDao.getUserById(firebaseUser.uid)
                                    withContext(Dispatchers.Main) {
                                        if (userInRoom != null) {
                                            navigateToNavHostActivity(false)
                                        } else {
                                            stopLoading()
                                            auth.signOut()
                                            Toast.makeText(
                                                this@LoginActivity,
                                                "המשתמש לא קיים באפליקציה. יש להירשם תחילה.",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                }
                            } else {
                                stopLoading()
                                Toast.makeText(this, "שגיאה בהתחברות", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            stopLoading()
                            Toast.makeText(this, "התחברות נכשלה. אנא נסה שוב.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "יש למלא אימייל וסיסמה", Toast.LENGTH_SHORT).show()
            }
        }

        signInButton.setOnClickListener {
            val intent = Intent(this, NavHostActivity::class.java)
            intent.putExtra("openSignUpFragment", true)
            startActivity(intent)
            finish()
        }
    }

    private fun navigateToNavHostActivity(openSignUp: Boolean) {
        val intent = Intent(this, NavHostActivity::class.java)
        intent.putExtra("openSignUpFragment", openSignUp)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
            finish()
        }, 1000)
    }

    private fun startLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        progressBar.visibility = View.GONE
    }
}
