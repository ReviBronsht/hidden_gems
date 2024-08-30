package com.example.hiddengems.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.RoundingMode


@Entity
//class to define Gems table
data class Gem(
    var uId:Int,
    val name:String,
    val desc:String,
    val address:String,
    val city:String,
    val type:String,
    var rating:Double,
    val ratings:MutableList<Int> = ArrayList(),
    @PrimaryKey(autoGenerate = true) val gId:Int = 0
)
