package com.example.hiddengems.Model

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hiddengems.base.MyApplication
import com.google.firebase.firestore.FieldValue

//class to define users table
@Entity
data class User (
    var user:String = "",
    var bio:String = "",
    var favoriteGems:MutableList<Int> = mutableListOf(),
    var visitedGems:MutableList<Int> = mutableListOf(),
    var image:String="",
    var email:String="",
    @PrimaryKey(autoGenerate = true) var uId:Int = 0
)
{
    companion object {

        //using shared references to define funcions that get and set lastUpdated to check for updates
        fun getLocalLastUpdate():Long{
            return MyApplication.Globals
                .appContext?.getSharedPreferences("TAG", Context.MODE_PRIVATE)
                ?.getLong("LAST_LOCAL_USER",0)?:0
        }
        fun setLastLocalUpdate(num:Long){
            MyApplication.Globals
                .appContext?.getSharedPreferences("TAG", Context.MODE_PRIVATE)?.edit()
                ?.putLong("LAST_LOCAL_USER",num)
                ?.apply()
        }

        //function that converts json to user
        fun fromJson(json: Map<String, Any>): User {
            val uId = (json.get("uId") as? Long)?.toInt() ?: 0
            val user = json.get("user").toString()
            val bio = json.get("bio").toString()
            val image = json.get("image").toString()
            val email = json.get("email").toString()
            val favoriteGems = json.get("favoriteGems") as MutableList<Int>
            val visitedGems = json.get("visitedGems") as MutableList<Int>
            val newUser = User(user, bio, favoriteGems, visitedGems, image,email, uId)
            return newUser
        }
    }

    //function to convert user to json with timestamp to check updates
    fun toJson(): HashMap<String, Any> {
        return hashMapOf(
            "uId" to uId,
            "user" to user,
            "bio" to bio,
            "image" to image,
            "email" to email,
            "favoriteGems" to favoriteGems,
            "visitedGems" to visitedGems,
            "lastUpdated" to FieldValue.serverTimestamp()
        )
    }
}