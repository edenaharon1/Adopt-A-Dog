package com.example.adoptadog

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.NavController

class NavHostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nav_host)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        val navController = navHostFragment?.navController

        val openSignUp = intent.getBooleanExtra("openSignUpFragment", false)
        if (openSignUp) {
            lifecycleScope.launchWhenResumed {
                navController?.navigate(R.id.fragmentSignUp)
                stopLoading() // ודואג להפסיק את הספינר
            }
        }
    }

    // ⬇️ פונקציות שליטה על הספינר
    fun startLoading() {
        findViewById<ProgressBar>(R.id.progressBar)?.visibility = View.VISIBLE
    }

    fun stopLoading() {
        findViewById<ProgressBar>(R.id.progressBar)?.visibility = View.GONE
    }
}