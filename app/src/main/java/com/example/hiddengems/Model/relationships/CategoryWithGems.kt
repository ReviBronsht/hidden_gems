package com.example.hiddengems.Model.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.hiddengems.Model.Category
import com.example.hiddengems.Model.Gem

//defines a 1-M relationship between category and gems
data class CategoryWithGems (
    @Embedded val category: Category,
    @Relation(
        parentColumn = "name",
        entityColumn = "type"
    )
    val gems: List<Gem>
    )