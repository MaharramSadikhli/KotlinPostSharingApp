package com.imsoft.kotlinpostsharingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.imsoft.kotlinpostsharingapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val currentUser = auth.currentUser

        if (currentUser != null) {
            val intent = Intent(this@MainActivity, GetPostActivity::class.java)
            startActivity(intent)
            finish()
        }
    }



    fun signInBtnClick(view: View) {
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val intent = Intent(this@MainActivity, GetPostActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this@MainActivity, exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
        } else if (email == "" && password.isNotEmpty()) {
            Toast.makeText(this@MainActivity, "Enter email!", Toast.LENGTH_LONG).show()
        } else if (email.isNotEmpty() && password == "") {
            Toast.makeText(this@MainActivity, "Enter password!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this@MainActivity, "Enter email and password!", Toast.LENGTH_LONG).show()
        }
    }
    fun signUpBtnClick(view: View) {
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val intent = Intent(this@MainActivity, GetPostActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this@MainActivity, exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
        } else if (email == "" && password.isNotEmpty()) {
            Toast.makeText(this@MainActivity, "Enter email!", Toast.LENGTH_LONG).show()
        } else if (password == "" && email.isNotEmpty()) {
            Toast.makeText(this@MainActivity, "Enter password!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this@MainActivity, "Enter email and password!", Toast.LENGTH_LONG).show()
        }

    }
}