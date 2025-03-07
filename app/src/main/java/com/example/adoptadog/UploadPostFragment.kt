package com.example.adoptadog

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
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
    private lateinit var uploadPostButton: Button
    private lateinit var imagePreview: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var postTextEditText: EditText
    private lateinit var uploadNowButton: Button
    private var selectedImageUri: Uri? = null

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                startLoading()
                selectedImageUri = uri
                imagePreview.setImageURI(uri)
                Handler(Looper.getMainLooper()).postDelayed({
                    stopLoading()
                }, 1000)
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
        uploadPostButton = view.findViewById(R.id.uploadPostButton)
        imagePreview = view.findViewById(R.id.imagePreview)
        progressBar = view.findViewById(R.id.progressBar)
        postTextEditText = view.findViewById(R.id.postDescription)
        uploadNowButton = view.findViewById(R.id.uploadPostButton)

        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        requestPermissionsIfNeeded() // קריאה לפונקציית בקשת ההרשאות

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
            startLoading()
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

    private fun requestPermissionsIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
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
                // Permission granted
            } else {
                Toast.makeText(requireContext(), "הרשאה נדחתה", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        progressBar.visibility = View.GONE
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
    }
}