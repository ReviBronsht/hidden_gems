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
    @PrimaryKey(autoGenerate = true) var uId:Int = 0
)
{
    fun fromJson(json: Map<String, Any>): User {
        val uId = json.get("uId") as Int
        val user = json.get("user").toString()
        val bio = json.get("bio").toString()
        val favoriteGems = json.get("favoriteGems")  as MutableList<Int>
        val visitedGems = json.get("visitedGems") as MutableList<Int>
        val newUser = User(user,bio,favoriteGems,visitedGems,uId)
        return newUser
    }
    fun toJson(): HashMap<String, Any> {
        val json = hashMapOf(
            "uId" to uId,
            "user" to user,
            "bio" to bio,
            "favoriteGems" to favoriteGems,
            "visitedGems" to visitedGems
        )
        return json
    }
}