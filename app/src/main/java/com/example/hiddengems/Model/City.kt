package com.example.hiddengems.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

//class to define gem cities table
@Entity
data class City (
    @PrimaryKey val name: String
){

    companion object {
        //function that converts json to city
        fun fromJson(json: Map<String, Any>): City {
            val name = json.get("name").toString()
            val newCity = City(name)
            return newCity
        }
    }

    //function to convert city to json
    fun toJson(): HashMap<String, Any> {
        return hashMapOf(
            "name" to name
        )
    }

}