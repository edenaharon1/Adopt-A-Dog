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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        progressBar = findViewById(R.id.progressBar)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)

        userDao = (application as MyApplication).database.userDao()

        val loginButton = findViewById<Button>(R.id.loginButton)
        val signInButton = findViewById<Button>(R.id.sign_in)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotBlank() && password.isNotBlank()) {
                startLoading()
                loginAndCheckUser(email, password)
            } else {
                Toast.makeText(this, "יש למלא אימייל וסיסמה", Toast.LENGTH_SHORT).show()
            }
        }

        signInButton.setOnClickListener {
            startLoading()
            navigateToNavHostActivity(true)
        }
    }

    private fun loginAndCheckUser(email: String, password: String) {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = FirebaseAuth.getInstance().currentUser
                    if (firebaseUser != null) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            val userInRoom = userDao.getUserById(firebaseUser.uid)
                            withContext(Dispatchers.Main) {
                                if (userInRoom != null) {
                                    navigateToNavHostActivity(false)
                                } else {
                                    stopLoading()
                                    FirebaseAuth.getInstance().signOut()
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
                    Toast.makeText(this, "פרטי ההתחברות שגויים", Toast.LENGTH_SHORT).show()
                }
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
