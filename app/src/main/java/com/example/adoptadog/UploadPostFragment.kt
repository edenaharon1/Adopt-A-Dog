package com.example.adoptadog

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.adoptadog.database.AppDatabase
import com.example.adoptadog.database.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UploadPostFragment : Fragment() {

    private lateinit var selectImageButton: Button
    private lateinit var uploadPostButton: Button
    private lateinit var imagePreview: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var postTextEditText: EditText
    private lateinit var uploadNowButton: Button
    private var selectedImageUri: Uri? = null

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upload_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<Button>(R.id.backButton)
        selectImageButton = view.findViewById(R.id.selectImageButton)
        uploadPostButton = view.findViewById(R.id.uploadPostButton)
        imagePreview = view.findViewById(R.id.imagePreview)
        progressBar = view.findViewById(R.id.progressBar)
        postTextEditText = view.findViewById(R.id.postDescription)
        uploadNowButton = view.findViewById(R.id.uploadPostButton)

        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        selectImageButton.setOnClickListener {
            selectImage()
        }

        uploadNowButton.setOnClickListener {
            uploadPost()
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                startLoading()
                selectedImageUri = uri
                imagePreview.setImageURI(uri)

                val contentResolver = requireContext().contentResolver
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                contentResolver.takePersistableUriPermission(uri, takeFlags)

                Handler(Looper.getMainLooper()).postDelayed({
                    stopLoading()
                }, 1000)
            }
        } else {
            Log.e("UploadPostFragment", "No image selected")
            Toast.makeText(requireContext(), "לא נבחרה תמונה", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadPost() {
        val postText = postTextEditText.text.toString()
        val imageUri = selectedImageUri
        if (postText.isNotEmpty() && imageUri != null) {
            startLoading()
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val post = Post(
                        imageUrl = imageUri.toString(),
                        userId = "1",
                        description = postText,
                        likes = emptyList(),
                        comments = mutableListOf(),
                        timestamp = System.currentTimeMillis()
                    )
                    AppDatabase.getDatabase(requireContext(), lifecycleScope).postDao().insert(post)
                    withContext(Dispatchers.Main) {
                        postTextEditText.text.clear()
                        stopLoading()
                        findNavController().navigate(R.id.action_uploadPostFragment_to_homePageFragment)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "שגיאה בהעלאת הפוסט", Toast.LENGTH_SHORT).show()
                        Log.e("UploadPostFragment", "Error uploading post: ${e.message}")
                        stopLoading()
                    }
                }
            }
        } else {
            Log.e("UploadPostFragment", "Text or image is missing")
        }
    }

    private fun startLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        progressBar.visibility = View.GONE
    }
}