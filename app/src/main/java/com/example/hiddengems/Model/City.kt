package com.example.hiddengems.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class City (
    @PrimaryKey val name: String
)