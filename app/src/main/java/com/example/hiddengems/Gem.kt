package com.example.hiddengems

data class Gem(
    val name:String,
    val desc:String,
    val address:String,
    val city:String,
    val type:String,
    val rating:Int = 0
)