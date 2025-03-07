package com.example.adoptadog

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        progressBar = findViewById(R.id.progressBar) // מציאת ה-ProgressBar מה-XML

        val loginButton = findViewById<Button>(R.id.loginButton)
        val signInButton = findViewById<Button>(R.id.sign_in)

        loginButton.setOnClickListener {
            startLoading() // הצגת טעינה
            navigateToNavHostActivity(false)
        }

        signInButton.setOnClickListener {
            startLoading() // הצגת טעינה
            navigateToNavHostActivity(true)
        }
    }

    private fun navigateToNavHostActivity(openSignUp: Boolean) {
        val intent = Intent(this, NavHostActivity::class.java)
        intent.putExtra("openSignUpFragment", openSignUp)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
            finish()
        }, 1500) // דיליי כדי לדמות טעינה של הנתונים
    }

    private fun startLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        progressBar.visibility = View.GONE
    }
}
