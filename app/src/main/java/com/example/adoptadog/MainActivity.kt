package com.example.dogadoptionapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.adoptadog.ProfileActivity
import com.example.adoptadog.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val title: TextView = findViewById(R.id.titleTextView)
        val loginButton: Button = findViewById(R.id.loginButton)
        val profileButton: Button = findViewById(R.id.profileButton)

        // Title setup
        title.text = "Dogs Available for Adoption"

        // Login button setup
        loginButton.setOnClickListener {
            showLoginDialog()
        }

        // Navigate to Profile Activity
        profileButton.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoginDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Login / Sign Up")

        // Inflate custom dialog layout with EditText for username and password and a Button for login
        val dialogView = LayoutInflater.from(this).inflate(R.layout.login_signup, null)
        builder.setView(dialogView)

        val usernameEditText = dialogView.findViewById<TextView>(R.id.usernameEditText)
        val passwordEditText = dialogView.findViewById<TextView>(R.id.passwordEditText)

        builder.setPositiveButton("Login") { _, _ ->
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Handle login functionality here (e.g., validate with server or local data)
                Toast.makeText(this, "Login clicked for $username", Toast.LENGTH_SHORT).show()

                // Dismiss the dialog and navigate to the main activity
                builder.create().dismiss()  // Use `builder.create()` instead of `dialog.dismiss()`
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Toast.makeText(this, "Please fill both fields", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()  // Directly create and show the dialog
    }
}
