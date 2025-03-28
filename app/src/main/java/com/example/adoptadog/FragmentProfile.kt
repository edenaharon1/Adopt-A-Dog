package com.example.adoptadog.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.adoptadog.LoginActivity
import com.example.adoptadog.MyApplication
import com.example.adoptadog.NavHostActivity
import com.example.adoptadog.R
import com.example.adoptadog.database.User
import com.example.adoptadog.database.UserDao
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentProfile : Fragment(R.layout.fragment_profile) {

    private lateinit var userNameText: TextView
    private lateinit var profileImageView: ImageView
    private lateinit var userDao: UserDao
    private var currentUser: User? = null

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            profileImageView.setImageURI(uri)
            Log.d("ProfileFragment", "Selected image URI: $uri")
            lifecycleScope.launch(Dispatchers.IO) {
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                if (firebaseUser != null) {
                    val user = userDao.getUserById(firebaseUser.uid)
                    user?.let {
                        currentUser = it.copy(profileImageUri = uri.toString())
                        userDao.insert(currentUser!!)
                        Log.d("ProfileFragment", "Image URI saved to DB: $uri")
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // ✨ מציאת הכפתורים

        val editPostsButton: Button = view.findViewById(R.id.myPostsButton)
        val returnToHP: Button = view.findViewById(R.id.backToHomeButton)
        val uploadPostButton = view.findViewById<Button>(R.id.uploadPostButton)
        val logoutButton = view.findViewById<Button>(R.id.logoutButton)
        val editProfileButton = view.findViewById<Button>(R.id.editProfileButton)

        userNameText = view.findViewById(R.id.userNameText)
        profileImageView = view.findViewById(R.id.profileImage)

        userDao = (requireActivity().application as MyApplication).database.userDao()

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            lifecycleScope.launch(Dispatchers.IO) {
                val user = userDao.getUserById(firebaseUser.uid)
                user?.let {
                    currentUser = it
                    withContext(Dispatchers.Main) {
                        userNameText.text = "Hello, ${it.name}!"
                        it.profileImageUri?.takeIf { uri -> uri.isNotBlank() }?.let { uri ->
                            try {
                                profileImageView.setImageURI(Uri.parse(uri))
                            } catch (e: Exception) {
                                Log.e("ProfileFragment", "Invalid URI: $uri", e)
                            }
                        }
                    }
                }
            }
        }

        profileImageView.setOnClickListener {
            pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        uploadPostButton.setOnClickListener {
            (activity as? NavHostActivity)?.startLoading()
            findNavController().navigate(R.id.action_FragmentProfile_to_uploadPostFragment)
        }

        editPostsButton.setOnClickListener {
            (activity as? NavHostActivity)?.startLoading()
            findNavController().navigate(R.id.action_profile_to_edit_posts)
        }

        returnToHP.setOnClickListener {
            (activity as? NavHostActivity)?.startLoading()
            findNavController().navigate(R.id.action_FragmentProfile_to_homePageFragment)
        }

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        editProfileButton.setOnClickListener {
            findNavController().navigate(R.id.action_FragmentProfile_to_fragmentEditProfile)
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("username")
            ?.observe(viewLifecycleOwner) { username ->
                userNameText.text = "Hello, $username!"
            }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Uri>("imageUri")
            ?.observe(viewLifecycleOwner) { imageUri ->
                if (imageUri != null) {
                    profileImageView.setImageURI(imageUri)
                }
            }
    }
}
