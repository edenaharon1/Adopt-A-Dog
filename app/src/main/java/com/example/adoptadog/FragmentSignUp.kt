package com.example.adoptadog

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


class FragmentSignUp : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

// הצגת הספינר בזמן טעינה
(activity as? NavHostActivity)?.startLoading()

Handler(Looper.getMainLooper()).postDelayed({
    (activity as? NavHostActivity)?.stopLoading()
}, 1000) // הסרת הספינר לאחר שנייה

val navController = findNavController() // קבלת NavController

view.findViewById<TextView>(R.id.loginButtonLink).setOnClickListener {
    val intent = Intent(requireContext(), LoginActivity::class.java)
    startActivity(intent)
    requireActivity().finish() // סגירת ה-Activity הנוכחי
}

view.findViewById<Button>(R.id.signupButton).setOnClickListener {
    navController.navigate(R.id.action_fragmentSignUp_to_homePageFragment) // ניווט ל-Home Fragment
}
    }
}
