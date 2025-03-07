package com.example.adoptadog

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class FragmentEditProfile : Fragment() {

    private lateinit var profileImageView: ImageView
    private var selectedImageUri: Uri? = null
    private lateinit var usernameEditText: EditText

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        profileImageView = view.findViewById(R.id.profileImageEdit)
        val changeImageButton: Button = view.findViewById(R.id.changeProfileImageButton)
        val backButton: Button = view.findViewById(R.id.backButton)
        val saveButton: Button = view.findViewById(R.id.saveChangesButton)
        usernameEditText = view.findViewById(R.id.usernameEditText)

        changeImageButton.setOnClickListener {
            openGallery()
        }

        backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        saveButton.setOnClickListener {
            saveChanges()
        }

        return view
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            profileImageView.setImageURI(selectedImageUri)
        }
    }

    private fun saveChanges() {
        val newUsername = usernameEditText.text.toString()
        val bundle = Bundle()
        bundle.putString("newUsername", newUsername)
        bundle.putParcelable("selectedImageUri", selectedImageUri) //הוספת שורה
        findNavController().previousBackStackEntry?.savedStateHandle?.set("username", newUsername)
        findNavController().previousBackStackEntry?.savedStateHandle?.set("imageUri", selectedImageUri) //הוספת שורה
        findNavController().popBackStack()
    }
}