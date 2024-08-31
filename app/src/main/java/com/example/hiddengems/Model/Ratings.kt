package com.example.hiddengems.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

//class to define gem cities table
@Entity(primaryKeys = ["gId","uId"])
data class Ratings (
    var gId: Int,
    var uId: Int,
    val ratingIdx: Int
){

    companion object {
        //function that converts json to ratings
        fun fromJson(json: Map<String, Any>): Ratings {
            val gId = (json.get("gId")as? Long)?.toInt() ?: 0
            val uId = (json.get("uId") as? Long)?.toInt() ?: 0
            val ratingIdx = (json.get("ratingIdx") as? Long)?.toInt() ?: 0
            val newRatings = Ratings(gId, uId, ratingIdx)
            return newRatings
        }
    }


    //function to convert rating to json
    fun toJson(): HashMap<String, Any> {
        return hashMapOf(
            "gId" to gId,
            "uId" to uId,
            "ratingIdx" to ratingIdx
        )
    }

}