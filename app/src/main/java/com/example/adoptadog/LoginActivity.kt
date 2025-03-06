package com.example.adoptadog

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val loginButton = findViewById<Button>(R.id.loginButton)
        val signInButton = findViewById<Button>(R.id.sign_in)

        loginButton.setOnClickListener {
            // ניווט רגיל ל-NavHostActivity (יפתח את ה-start destination)
            navigateToNavHostActivity(false)
        }

        signInButton.setOnClickListener {
            // ניווט ל-NavHostActivity עם דגל לפתיחת FragmentSignUp
            navigateToNavHostActivity(true)
        }
    }

    private fun navigateToNavHostActivity(openSignUp: Boolean) {
        val intent = Intent(this, NavHostActivity::class.java)
        intent.putExtra("openSignUpFragment", openSignUp)
        startActivity(intent)
        finish()
    }
}