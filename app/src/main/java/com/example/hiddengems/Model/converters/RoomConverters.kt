package com.example.hiddengems.Model.converters

import androidx.room.TypeConverter
import com.example.hiddengems.Model.Comment

class RoomConverters {

    @TypeConverter
    fun convertRatingsToString(ratings:MutableList<Int>):String{
        return ratings.joinToString(separator = ",")
    }

    @TypeConverter
    fun convertStringToRatings(ratingString: String):MutableList<Int>{
        return ratingString.split(",").map { it.toInt() } as MutableList<Int>
    }

//    @TypeConverter
//    fun convertCommentToString(comments:MutableList<Comment>):String{
//        return "hoi"
//    }
//
//    @TypeConverter
//    fun convertStringToComment(comment:String):MutableList<Comment>{
//        return mutableListOf( Comment("hi","hoi"))
//    }

}