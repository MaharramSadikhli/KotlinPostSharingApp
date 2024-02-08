package com.imsoft.kotlinpostsharingapp.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.imsoft.kotlinpostsharingapp.databinding.ActivityAddPostBinding
import java.util.UUID

class AddPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPostBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var selectedImage: Uri? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerLauncher()

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage
    }

    fun postBtnClick(view: View) {

        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val storageRef = storage.reference

        val imagesRef = storageRef.child("images")
        val imageRef = imagesRef.child(imageName)

        val uploadTask = imageRef.putFile(selectedImage!!) // check null


        if (selectedImage != null) {

            uploadTask
                .addOnSuccessListener {

                    imageRef.downloadUrl
                        .addOnSuccessListener {
                            val downloadUrl = it.toString()
                            val userEmail = auth.currentUser!!.email
                            val userComment = binding.commentText.text.toString()

                            if (auth.currentUser != null) {
                                val postMap = hashMapOf<String, Any>()

                                postMap["userEmail"] = userEmail!!
                                postMap["userComment"] = userComment
                                postMap["downloadUrl"] = downloadUrl
                                postMap["date"] = Timestamp.now()

                                firestore.collection("Posts").add(postMap)
                                    .addOnSuccessListener {
                                        finish()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this@AddPostActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
                                    }
                            }
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(this@AddPostActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
                }

        }

    }
    fun selectImage(view: View) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this@AddPostActivity, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this@AddPostActivity, Manifest.permission.READ_MEDIA_IMAGES)) {
                    Snackbar.make(view, "Permission Needed For Galery!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK!") {
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        }
                        .show()
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }

            } else {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intent)
            }
        } else {
            if (ContextCompat.checkSelfPermission(this@AddPostActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this@AddPostActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Snackbar.make(view, "Permission Needed For Galery!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK!") {
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                        .show()
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }

            } else {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intent)
            }
        }

    }

    private fun registerLauncher() {

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result->

            if (result.resultCode == RESULT_OK) {
                val intent = result.data

                if (intent != null) {
                    selectedImage = intent.data
                    selectedImage?. let {
                        binding.imageView.setImageURI(it)
                    }
                }
            }

        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result->

            if (result) {

                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intent)

            } else {
                Toast.makeText(this@AddPostActivity, "Permission Needed!", Toast.LENGTH_LONG).show()
            }

        }

    }

}