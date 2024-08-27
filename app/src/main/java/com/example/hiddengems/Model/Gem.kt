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
    var myRatingIdx: Int,
    val ratings:MutableList<Int> = ArrayList(),
    val comments:MutableList<Comment> = ArrayList()
) {
    //function that recalculates rating when user adds rating from all previous ratings and new rating average
    //if user previously rated gem (has index of rating), updates at index, if doesn't, adds new rating
    fun updateRating(nRating: Int){
            if (myRatingIdx == -1) {
                ratings.add(nRating)
                myRatingIdx = ratings.size-1
            }
            else {
                ratings[myRatingIdx] = nRating
            }
            rating = ratings.average().toBigDecimal().setScale(1,RoundingMode.DOWN).toDouble()

    }

//    fun addRating(rating:Int){
//
//        ratings.add(rating)
//    }
}