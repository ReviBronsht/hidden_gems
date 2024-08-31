package com.example.hiddengems.Model

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hiddengems.base.MyApplication
import com.google.firebase.firestore.FieldValue
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
    val image:String,
    var rating:Double,
    val ratings:MutableList<Int> = ArrayList(),
    @PrimaryKey(autoGenerate = true) var gId:Int = 0,

) {

    companion object {

        //using shared references to define funcions that get and set lastUpdated to check for updates
        fun getLocalLastUpdate():Long{
            return MyApplication.Globals
                .appContext?.getSharedPreferences("TAG",Context.MODE_PRIVATE)
                ?.getLong("LAST_LOCAL_GEM",0)?:0
        }
        fun setLastLocalUpdate(num:Long){
            MyApplication.Globals
                .appContext?.getSharedPreferences("TAG", Context.MODE_PRIVATE)?.edit()
                ?.putLong("LAST_LOCAL_GEM",num)
                ?.apply()
        }
        //function that converts json to gem
        fun fromJson(json: Map<String, Any>): Gem {
            val gId = (json.get("gId") as? Long)?.toInt() ?: 0
            val uId = (json.get("uId") as? Long)?.toInt() ?: 0
            val name = json.get("name").toString()
            val desc = json.get("desc").toString()
            val address = json.get("address").toString()
            val city = json.get("city").toString()
            val type = json.get("type").toString()
            val image = json.get("image").toString()
            val rating = json.get("rating") as Double
            val ratings = json.get("ratings") as MutableList<Int>
            val newGem = Gem(uId, name, desc, address, city, type, image, rating, ratings, gId)
            return newGem
        }
    }

    //function to convert gem to json with timestamp to check updates
    fun toJson(): HashMap<String, Any> {
        return hashMapOf(
            "gId" to gId,
            "uId" to uId,
            "name" to name,
            "desc" to desc,
            "address" to address,
            "city" to city,
            "type" to type,
            "image" to image,
            "rating" to rating,
            "ratings" to ratings,
            "lastUpdated" to FieldValue.serverTimestamp()
        )
    }

}
