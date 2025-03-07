package com.example.adoptadog.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.adoptadog.NavHostActivity
import com.example.adoptadog.R

class EditPostsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // הצגת הספינר בזמן טעינה
        (activity as? NavHostActivity)?.startLoading()

        Handler(Looper.getMainLooper()).postDelayed({
            (activity as? NavHostActivity)?.stopLoading()
        }, 1000) // זמן ההמתנה לפני שהספינר נעלם

        val exitButton: Button = view.findViewById(R.id.exit)

        exitButton.setOnClickListener {
            findNavController().navigate(R.id.action_editPostsFragment_to_FragmentProfile)
        }
    }
}
