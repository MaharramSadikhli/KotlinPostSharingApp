package com.imsoft.kotlinpostsharingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.imsoft.kotlinpostsharingapp.databinding.ActivityAddPostBinding

class AddPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun postBtnClick(view: View) {}
    fun selectImage(view: View) {}
}