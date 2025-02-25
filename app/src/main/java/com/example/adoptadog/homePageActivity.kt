package com.example.adoptadog

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class homePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        // חיבור הכפתורים מה-XML
        val profileIcon: ImageView = findViewById(R.id.profileIcon)
        val logoutButton: Button = findViewById(R.id.logoutButton)

        // מעבר לדף הפרופיל בלחיצה על האייקון
        profileIcon.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        // יציאה מהחשבון בלחיצה על כפתור "Log Out"
        logoutButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // מסיים את המסך כדי למנוע חזרה אחורה
        }

        // שומר על תצוגה תקינה עם מערכת ההפעלה
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
