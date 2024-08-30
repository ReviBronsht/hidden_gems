package com.example.hiddengems.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

//class to define Comments
@Entity
data class Comment (
    var gId: Int,
    var user: String,
    val comment: String,
    @PrimaryKey(autoGenerate = true) var comId:Int = 0,
)