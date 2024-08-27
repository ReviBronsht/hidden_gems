package com.example.hiddengems.Model

data class User (
    val user:String,
    val bio:String = "",
    val favoriteGems:MutableList<Int> = mutableListOf(),
    val visitedGems:MutableList<Int> = mutableListOf()
)