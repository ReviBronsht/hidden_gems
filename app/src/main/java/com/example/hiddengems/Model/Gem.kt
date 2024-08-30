package com.example.hiddengems.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.RoundingMode

@Entity
//class to define Gems
data class Gem(
    var user:String,
    val name:String,
    val desc:String,
    val address:String,
    val city:String,
    val type:String,
    var rating:Double,
    var myRatingIdx: Int,
    val ratings:MutableList<Int> = ArrayList(),
    @PrimaryKey(autoGenerate = true) val gId:Int = 0
)
//{

//    fun addRating(rating:Int){
//
//        ratings.add(rating)
//    }
//}