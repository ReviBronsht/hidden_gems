package com.example.hiddengems.Model

data class User (
    var user:String,
    var bio:String = "",
    val favoriteGems:MutableList<Int> = mutableListOf(),
    val visitedGems:MutableList<Int> = mutableListOf()
)