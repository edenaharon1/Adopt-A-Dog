package com.example.adoptadog

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.adoptadog.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signInButton = findViewById<Button>(R.id.sign_in)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            navigateToHomePage()
           /* // בדיקת אם האימייל והסיסמה תקינים
            if (validateLogin(email, password)) {
                // פעולה עבור הכניסה של המשתמש (בהמשך - חיבור ל-Firebase)
                // התחברות עם Firebase
                navigateToHomePage() // מעבר לדף הבית אם הלוג אין תקין
            } else {
                // הצגת הודעת שגיאה אם הלוג אין לא תקין
                Toast.makeText(this, "אימייל או סיסמה לא תקינים", Toast.LENGTH_SHORT).show()
            }
            */

        }

        signInButton.setOnClickListener {
            // פעולה עבור כפתור "Sign In" - (להיות מאומת מול Firebase או ליצור משתמש חדש)
        }
    }

    //פונקציית העברה מדף הlogin to homepage
    private fun navigateToHomePage() {
            val intent = Intent(this, homePageActivity::class.java)
            startActivity(intent)
            finish()
    }

    // פונקציה לאימות דואר אלקטרוני
    private fun validateEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}"
        return email.matches(emailPattern.toRegex())
    }

    // פונקציה לאימות סיסמה
    private fun validatePassword(password: String): Boolean {
        return password.length >= 6 // אורך מינימלי 6 תוים
    }

    // פונקציה לאימות כלליים של דואר אלקטרוני וסיסמה
    private fun validateLogin(email: String, password: String): Boolean {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!validateEmail(email)) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!validatePassword(password)) {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
