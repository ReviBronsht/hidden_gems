package com.example.hiddengems.Model.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.hiddengems.Model.Category
import com.example.hiddengems.Model.Gem
import com.example.hiddengems.Model.User

//defines a 1-M relationship between user and gems
data class UserWithGems (
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "uId",
        entityColumn = "uId"
    )
    val gems: List<Gem>
)