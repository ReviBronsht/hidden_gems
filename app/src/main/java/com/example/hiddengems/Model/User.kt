package com.example.hiddengems.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

//class to define users table
@Entity
data class User (
    var user:String = "",
    var bio:String = "",
    var favoriteGems:MutableList<Int> = mutableListOf(),
    var visitedGems:MutableList<Int> = mutableListOf(),
    var image:String="",
    @PrimaryKey(autoGenerate = true) var uId:Int = 0
)
{
    companion object {
        //function that converts json to user
        fun fromJson(json: Map<String, Any>): User {
            val uId = (json.get("uId") as? Long)?.toInt() ?: 0
            val user = json.get("user").toString()
            val bio = json.get("bio").toString()
            val image = json.get("image").toString()
            val favoriteGems = json.get("favoriteGems") as MutableList<Int>
            val visitedGems = json.get("visitedGems") as MutableList<Int>
            val newUser = User(user, bio, favoriteGems, visitedGems, image, uId)
            return newUser
        }
    }

    //function to convert user to json
    fun toJson(): HashMap<String, Any> {
        return hashMapOf(
            "uId" to uId,
            "user" to user,
            "bio" to bio,
            "image" to image,
            "favoriteGems" to favoriteGems,
            "visitedGems" to visitedGems
        )
    }
}