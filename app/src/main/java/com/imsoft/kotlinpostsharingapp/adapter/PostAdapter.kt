package com.imsoft.kotlinpostsharingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imsoft.kotlinpostsharingapp.databinding.RecyclerRowBinding
import com.imsoft.kotlinpostsharingapp.model.Post

class PostAdapter(val postList: ArrayList<Post>): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(val binding: RecyclerRowBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerRowBinding.inflate(layoutInflater, parent, false)

        return PostViewHolder(binding)
    }


    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.binding.recyclerViewEmail.text = postList[position].email
        holder.binding.recyclerViewComment.text = postList[position].comment
    }

    override fun getItemCount(): Int {
        return postList.size
    }

}