package com.example.hiddengems.Model

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hiddengems.base.MyApplication
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue

//class to define Comments table
@Entity
data class Comment (
    var gId: Int,
    var uId: Int,
    val comment: String,
    @PrimaryKey(autoGenerate = true) var comId:Int = 0,
    var lastUpdated: Long = 0
){

    companion object {

        //using shared references to define funcions that get and set lastUpdated to check for updates
        fun getLocalLastUpdate():Long{
            return MyApplication.Globals
                .appContext?.getSharedPreferences("TAG", Context.MODE_PRIVATE)
                ?.getLong("LAST_LOCAL_COMMENT",0)?:0
        }
        fun setLastLocalUpdate(num:Long){
            MyApplication.Globals
                .appContext?.getSharedPreferences("TAG", Context.MODE_PRIVATE)?.edit()
                ?.putLong("LAST_LOCAL_COMMENT",num)
                ?.apply()
        }

        //function that converts json to comment
        fun fromJson(json: Map<String, Any>): Comment {
            val comId = (json.get("comId") as? Long)?.toInt() ?: 0
            val gId = (json.get("gId")as? Long)?.toInt() ?: 0
            val uId = (json.get("uId") as? Long)?.toInt() ?: 0
            val comment = json.get("comment").toString()
            var time:Timestamp = json.get("lastUpdated") as Timestamp
            val newComment = Comment(gId, uId, comment, comId,time.seconds)
            return newComment
        }
    }

    //function to convert comment to json with timestamp to check updates
    fun toJson(): HashMap<String, Any> {
        return hashMapOf(
            "comId" to comId,
            "gId" to gId,
            "uId" to uId,
            "comment" to comment,
            "lastUpdated" to FieldValue.serverTimestamp()
        )
    }

}