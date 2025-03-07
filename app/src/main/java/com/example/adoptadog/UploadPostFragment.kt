package com.example.adoptadog

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
    private lateinit var imagePreview: ImageView
    private lateinit var postTextEditText: EditText
    private lateinit var uploadNowButton: Button
    private var selectedImageUri: Uri? = null

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                selectedImageUri = uri
                imagePreview.setImageURI(uri)
            } else {
                Log.e("UploadPostFragment", "No image selected")
            }
        }

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
        imagePreview = view.findViewById(R.id.imagePreview)
        postTextEditText = view.findViewById(R.id.postDescription)
        uploadNowButton = view.findViewById(R.id.uploadPostButton)

        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        requestPermissions()

        selectImageButton.setOnClickListener {
            pickImage.launch("image/*")
        }

        uploadNowButton.setOnClickListener {
            uploadPost()
        }
    }

    private fun uploadPost() {
        val postText = postTextEditText.text.toString()
        val imageUri = selectedImageUri
        if (postText.isNotEmpty() && imageUri != null) {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val post = Post(
                        imageUrl = imageUri.toString(),
                        userId = "1",
                        description = postText,
                        likes = emptyList(),
                        comments = emptyList(),
                        timestamp = System.currentTimeMillis()
                    )
                    AppDatabase.getDatabase(requireContext(), lifecycleScope).postDao().insert(post)
                    withContext(Dispatchers.Main) {
                        postTextEditText.text.clear() // ניקוי ה-EditText
                        findNavController().navigateUp()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main){
                        Toast.makeText(requireContext(), "שגיאה בהעלאת הפוסט", Toast.LENGTH_SHORT).show()
                    }
                    Log.e("UploadPostFragment", "Error uploading post: ${e.message}")
                }
            }
        } else {
            Log.e("UploadPostFragment", "Text or image is missing")
        }
    }

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // הרשאה ניתנה
            } else {
                // הרשאה נדחתה
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
    }
}