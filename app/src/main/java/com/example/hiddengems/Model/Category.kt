package com.example.hiddengems.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

//class to define gem categories(types) table
@Entity
data class Category (
    @PrimaryKey val name: String,
    val icon: String
    ) {

    companion object {
        //function that converts json to category
        fun fromJson(json: Map<String, Any>): Category {
            val name = json.get("name").toString()
            val icon = json.get("icon").toString()
            val newCategory = Category(name, icon)
            return newCategory
        }
    }

    //function to convert category to json
    fun toJson(): HashMap<String, Any> {
        return hashMapOf(
            "name" to name,
            "icon" to icon
        )
    }

}