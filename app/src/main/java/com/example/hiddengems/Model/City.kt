package com.example.hiddengems.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

//class to define gem cities table
@Entity
data class City (
    @PrimaryKey val name: String
)