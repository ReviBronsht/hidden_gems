package com.example.hiddengems.dao

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hiddengems.Model.Category
import com.example.hiddengems.Model.City
import com.example.hiddengems.Model.Comment
import com.example.hiddengems.Model.Gem
import com.example.hiddengems.Model.converters.RoomConverters
import com.example.hiddengems.Model.relationships.CategoryWithGems
import com.example.hiddengems.base.MyApplication


@Database(entities = [Category::class,Gem::class,Comment::class, City::class], version = 15)
@TypeConverters(RoomConverters::class)
abstract class AppLocalDbRepository : RoomDatabase() {
    abstract fun hiddenGemsDao(): HiddenGemsDao
}
object AppLocalDatabase {
    val db: AppLocalDbRepository by lazy {

        val context = MyApplication.Globals.appContext
            ?: throw IllegalStateException("Application context not available")

        Room.databaseBuilder(
            context,
            AppLocalDbRepository::class.java,
            "dbFileName.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}