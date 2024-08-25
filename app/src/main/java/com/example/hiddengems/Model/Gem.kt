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
    val rating:Double,
    val ratings:MutableList<Int> = ArrayList()
) {
    //function that recalculates rating when user adds rating from all previous ratings and new rating average
    fun updateRating(nRating: Int):Double{
        if (ratings.size == 0){
            return 0.0
        }
        else {
            ratings.add(nRating)
            return ratings.average().toBigDecimal().setScale(1,RoundingMode.DOWN).toDouble()
        }
    }

//    fun addRating(rating:Int){
//
//        ratings.add(rating)
//    }
}