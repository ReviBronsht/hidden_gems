package com.example.hiddengems.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

//class to define gem cities table
@Entity(primaryKeys = ["gId","uId"])
data class Ratings (
    var gId: Int,
    var uId: Int,
    val ratingIdx: Int
)