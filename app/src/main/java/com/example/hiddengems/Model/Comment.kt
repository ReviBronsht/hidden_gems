package com.example.hiddengems.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

//class to define Comments table
@Entity
data class Comment (
    var gId: Int,
    var uId: Int,
    val comment: String,
    @PrimaryKey(autoGenerate = true) var comId:Int = 0,
){
    companion object {
        //function that converts json to comment
        fun fromJson(json: Map<String, Any>): Comment {
            val comId = (json.get("comId") as? Long)?.toInt() ?: 0
            val gId = (json.get("gId")as? Long)?.toInt() ?: 0
            val uId = (json.get("uId") as? Long)?.toInt() ?: 0
            val comment = json.get("comment").toString()
            val newComment = Comment(gId, uId, comment, comId)
            return newComment
        }
    }

    //function to convert comment to json
    fun toJson(): HashMap<String, Any> {
        return hashMapOf(
            "comId" to comId,
            "gId" to gId,
            "uId" to uId,
            "comment" to comment
        )
    }

}