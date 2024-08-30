package com.example.hiddengems.base

import android.app.Application
import android.content.Context

//class that provides the application a global way to access app context throughout the app
class MyApplication: Application() {

    object Globals{
        var appContext: Context?= null
    }

    override fun onCreate() {
        super.onCreate()
        Globals.appContext = applicationContext
    }
}