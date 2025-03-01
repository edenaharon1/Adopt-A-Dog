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

        loginButton.setOnClickListener {
            navigateToNavHostActivity() // מעבר ישירות ל-NavHostActivity
        }
    }

    // פונקציית העברה מדף הlogin ל-NavHostActivity
    private fun navigateToNavHostActivity() {
        val intent = Intent(this, NavHostActivity::class.java)
        startActivity(intent)
        finish() // לסיים את הפעילות הנוכחית כך שלא ניתן לחזור אליה על ידי כפתור Back
    }
}