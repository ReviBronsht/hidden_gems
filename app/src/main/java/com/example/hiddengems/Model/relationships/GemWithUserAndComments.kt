package com.example.hiddengems.Model.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.hiddengems.Model.Gem
import com.example.hiddengems.Model.User
import com.example.hiddengems.Model.views.CommentWithUser

//defines a 1-1 relationship between gem and user and 1-M between gem and commentwithuser view
data class GemWithUserAndComments (
    @Embedded val gem: Gem,
    @Relation(
        parentColumn = "uId",
        entityColumn = "uId"
    )
    val user: User,
    @Relation(
        parentColumn = "gId",
        entityColumn = "gId"
    )
    val commentsWithUsers: List<CommentWithUser>
)