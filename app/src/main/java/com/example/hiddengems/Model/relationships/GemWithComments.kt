package com.example.hiddengems.Model.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.hiddengems.Model.Category
import com.example.hiddengems.Model.Comment
import com.example.hiddengems.Model.Gem

data class GemWithComments (
    @Embedded
    val gem: Gem,
    @Relation(
        parentColumn = "gId",
        entityColumn = "gId"
    )
    val comments: List<Comment>
)