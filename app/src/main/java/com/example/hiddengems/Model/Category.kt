package com.example.hiddengems.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

//class to define gem categories(types) table
@Entity
data class Category (
    @PrimaryKey val name: String,
    val icon: String
    )