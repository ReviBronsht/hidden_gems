package com.example.hiddengems.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

//class to define users table
@Entity
data class User (
    var user:String = "",
    var bio:String = "",
    var favoriteGems:MutableList<Int> = mutableListOf(),
    var visitedGems:MutableList<Int> = mutableListOf(),
    @PrimaryKey(autoGenerate = true) var uId:Int = 0
)