package com.example.hiddengems.Model

import java.math.RoundingMode

//class to define Gems
data class Gem(
    val id:Int,
    val user:String,
    val name:String,
    val desc:String,
    val address:String,
    val city:String,
    val type:String,
    var rating:Double,
    val ratings:MutableList<Int> = ArrayList(),
    val comments:MutableList<Comment> = ArrayList()
) {
    //function that recalculates rating when user adds rating from all previous ratings and new rating average
    fun updateRating(nRating: Int){
        if (ratings.size == 0){
            rating = 0.0
        }
        else {
            ratings.add(nRating)
            rating = ratings.average().toBigDecimal().setScale(1,RoundingMode.DOWN).toDouble()
        }
    }

//    fun addRating(rating:Int){
//
//        ratings.add(rating)
//    }
}