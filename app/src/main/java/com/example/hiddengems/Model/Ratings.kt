package com.example.hiddengems.Model

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hiddengems.base.MyApplication
import com.google.firebase.firestore.FieldValue

//class to define gem cities table
@Entity(primaryKeys = ["gId","uId"])
data class Ratings (
    var gId: Int,
    var uId: Int,
    val ratingIdx: Int
){

    companion object {

        //using shared references to define funcions that get and set lastUpdated to check for updates
        fun getLocalLastUpdate():Long{
            return MyApplication.Globals
                .appContext?.getSharedPreferences("TAG", Context.MODE_PRIVATE)
                ?.getLong("LAST_LOCAL_RATING",0)?:0
        }
        fun setLastLocalUpdate(timestamp:Long){
            MyApplication.Globals
                .appContext?.getSharedPreferences("TAG", Context.MODE_PRIVATE)?.edit()
                ?.putLong("LAST_LOCAL_RATING",timestamp)
                ?.apply()
        }

        //function that converts json to ratings
        fun fromJson(json: Map<String, Any>): Ratings {
            val gId = (json.get("gId")as? Long)?.toInt() ?: 0
            val uId = (json.get("uId") as? Long)?.toInt() ?: 0
            val ratingIdx = (json.get("ratingIdx") as? Long)?.toInt() ?: 0
            val newRatings = Ratings(gId, uId, ratingIdx)
            return newRatings
        }
    }


    //function to convert rating to json with timestamp to check updates
    fun toJson(): HashMap<String, Any> {
        return hashMapOf(
            "gId" to gId,
            "uId" to uId,
            "ratingIdx" to ratingIdx,
            "lastUpdated" to FieldValue.serverTimestamp()
        )
    }

}