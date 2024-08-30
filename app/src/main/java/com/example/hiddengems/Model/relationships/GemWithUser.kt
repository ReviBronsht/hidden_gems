package com.example.hiddengems.Model.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.hiddengems.Model.Gem
import com.example.hiddengems.Model.User

//defines a 1-1 relationship between gem and user
data class GemWithUser (
    @Embedded val gem: Gem,
    @Relation(
    parentColumn = "uId",
    entityColumn = "uId"
)
val user: User

)