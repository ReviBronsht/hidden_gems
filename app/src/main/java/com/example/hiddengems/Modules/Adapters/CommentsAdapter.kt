package com.example.hiddengems.Modules.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hiddengems.Model.Comment
import com.example.hiddengems.Model.User
import com.example.hiddengems.Model.views.CommentWithUser
import com.example.hiddengems.R
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class CommentsAdapter  (

    //setting parameters as comment with user list
    private var comments:MutableList<CommentWithUser>

): RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>(){
    private lateinit var context: Context
    //CommentsViewHolder holds references to the views for each item
    inner class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var tvUserName: TextView?=null
        var tvComment: TextView?= null
        var ivUserImg:ShapeableImageView?=null

        init {
            tvUserName = itemView.findViewById<TextView>(R.id.tvUserName)
            tvComment = itemView.findViewById<TextView>(R.id.tvComment)
            ivUserImg = itemView.findViewById<ShapeableImageView>(R.id.ivUserImg)
        }
    }

    //OnCreateViewHolder creates and returns a CommentsViewHolder for a new item
    //inflates the layout and initializes context from parent.context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        context = parent.context
        return CommentsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_comment,
                parent,
                false
            )
        )
    }

    //function that adds a new comment to the beginning of comments list
    fun addComment(comment: Comment,user: User){
        val newCommentWithUser = CommentWithUser(comment.gId,comment.comId,comment.comment,user.uId,user.user,user.image)
       comments.add(0,newCommentWithUser)
        notifyItemInserted(0)
    }

    //onBindViewHolder binds data to CommentsViewHolder
    //finds the current comment from list by position
    // uses it to set the user, and comment
    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val currComment = comments[position]
        holder.tvUserName?.text =  currComment.user
        holder.tvComment?.text =  currComment.comment
        if (currComment.image!="") {
            Picasso.with(context).load(currComment.image).fit().centerCrop().into(holder.ivUserImg)
        }
    }

    //returns the number of items in comments list so recyclerview determines how many items to display
    override fun getItemCount(): Int {
        return comments.size
    }

}
