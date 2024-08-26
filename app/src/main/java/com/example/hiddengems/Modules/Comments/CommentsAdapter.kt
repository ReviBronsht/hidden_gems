package com.example.hiddengems.Modules.Comments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hiddengems.Model.Comment
import com.example.hiddengems.Model.Gem
import com.example.hiddengems.Modules.Gems.GemsAdapter
import com.example.hiddengems.R
import com.google.android.material.imageview.ShapeableImageView

class CommentsAdapter  (

    //setting parameters as comments list
    private var comments:MutableList<Comment>

): RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>(){

    //CommentsViewHolder holds references to the views for each item
    inner class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var tvUserName: TextView?=null
        var tvComment: TextView?= null

        init {
            tvUserName = itemView.findViewById<TextView>(R.id.tvUserName)
            tvComment = itemView.findViewById<TextView>(R.id.tvComment)
        }
    }

    //OnCreateViewHolder creates and returns a CommentsViewHolder for a new item
    //inflates the layout and initializes context from parent.context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        return CommentsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_comment,
                parent,
                false
            )
        )
    }

    //function that adds a new comment to the beginning of comments list
    fun addComment(comment: Comment){
       comments.add(0,comment)
        notifyItemInserted(0)
    }

    //onBindViewHolder binds data to CommentsViewHolder
    //finds the current comment from list by position
    // uses it to set the user, and comment
    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val currGem = comments[position]
        holder.tvUserName?.text =  currGem.user
        holder.tvComment?.text =  currGem.comment
    }

    //returns the number of items in comments list so recyclerview determines how many items to display
    override fun getItemCount(): Int {
        return comments.size
    }

}
