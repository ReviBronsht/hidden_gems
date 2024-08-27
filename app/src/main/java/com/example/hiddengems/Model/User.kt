package com.example.hiddengems.Model

data class User (
    var user:String = "",
    var bio:String = "",
    var favoriteGems:MutableList<Int> = mutableListOf(),
    var visitedGems:MutableList<Int> = mutableListOf()
)