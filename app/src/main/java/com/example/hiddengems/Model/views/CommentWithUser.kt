package com.example.hiddengems.Model.views

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Relation
import com.example.hiddengems.Model.Comment
import com.example.hiddengems.Model.User


//view that connects comments and users to be used in querying
@DatabaseView(
    viewName = "CommentWithUserView",
    value = """
        SELECT c.gId, c.comId, c.comment, c.uId, u.user, u.bio
        
        FROM Comment c
        INNER JOIN User u ON c.uId = u.uId
    """
)
data class CommentWithUser(
    val gId: Int,
    val comId: Int,
    val comment: String,
    val uId: Int,
    val user: String, // User's name from the User table
    val bio: String   // User's bio from the User table
)