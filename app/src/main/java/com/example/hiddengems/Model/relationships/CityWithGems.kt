package com.example.hiddengems.Model.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.hiddengems.Model.Category
import com.example.hiddengems.Model.City
import com.example.hiddengems.Model.Gem

//defines a 1-M relationship between city and gems
data class CityWithGems (
    @Embedded
    val city: City,
    @Relation(
        parentColumn = "name",
        entityColumn = "city"
    )
    val gems: List<Gem>
)