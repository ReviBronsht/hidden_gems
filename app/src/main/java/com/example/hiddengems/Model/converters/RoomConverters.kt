package com.example.hiddengems.Model.converters

import androidx.room.TypeConverter
import com.example.hiddengems.Model.Comment

//type converters so room handles lists of ints
class RoomConverters {

    @TypeConverter
    fun convertIntsToString(ints:MutableList<Int>):String{
        return ints.joinToString(separator = ",")
    }

    @TypeConverter
    fun convertStringToInts(str: String):MutableList<Int>{
        if (str.isNullOrEmpty()) {
            return mutableListOf<Int>()
        }
        else {
            return str.split(",").map { it.toInt() } as MutableList<Int>
        }
    }

}