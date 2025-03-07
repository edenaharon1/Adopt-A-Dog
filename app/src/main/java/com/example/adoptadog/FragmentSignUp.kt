package com.example.adoptadog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.adoptadog.NavHostActivity

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
    }
}
