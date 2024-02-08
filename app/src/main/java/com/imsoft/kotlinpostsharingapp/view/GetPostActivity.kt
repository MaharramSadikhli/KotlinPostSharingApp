package com.imsoft.kotlinpostsharingapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.imsoft.kotlinpostsharingapp.R
import com.imsoft.kotlinpostsharingapp.adapter.PostAdapter
import com.imsoft.kotlinpostsharingapp.databinding.ActivityGetPostBinding
import com.imsoft.kotlinpostsharingapp.model.Post

class GetPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGetPostBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var postArrayList: ArrayList<Post>
    private lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        firestore = Firebase.firestore

        postArrayList = ArrayList<Post>()

        getPost()

        binding.recyclerView.layoutManager = LinearLayoutManager(this@GetPostActivity)
        postAdapter = PostAdapter(postArrayList)
        binding.recyclerView.adapter = postAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater: MenuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_post_sharing_app, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_post) {
            val intent = Intent(this@GetPostActivity, AddPostActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            auth.signOut()
            val intent = Intent(this@GetPostActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getPost() {
        
        firestore.collection("Posts").addSnapshotListener { value, error ->

            if (error == null) {
                if (value != null && !(value.isEmpty)) {
                    val documents = value.documents

                    for (document in documents) {

                        val userEmail = document["userEmail"] as String
                        val userComment = document["userComment"] as String
                        val downloadUrl = document["downloadUrl"] as String

                        println(userComment)

                        val post = Post(userEmail, userComment, downloadUrl)

                        postArrayList.add(post)

                    }

                    postAdapter.notifyDataSetChanged()
                }
            } else {
                Toast.makeText(this@GetPostActivity, error.localizedMessage, Toast.LENGTH_LONG).show()
            }

        }
        
    }


}