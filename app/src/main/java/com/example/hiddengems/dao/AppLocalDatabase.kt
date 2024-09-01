package com.example.hiddengems.dao

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hiddengems.Model.Category
import com.example.hiddengems.Model.City
import com.example.hiddengems.Model.Comment
import com.example.hiddengems.Model.Gem
import com.example.hiddengems.Model.Ratings
import com.example.hiddengems.Model.User
import com.example.hiddengems.Model.converters.RoomConverters
import com.example.hiddengems.Model.views.CommentWithUser
import com.example.hiddengems.base.MyApplication


//defining local room database tables, views and version
@Database(entities = [Category::class,Gem::class,Comment::class, City::class, User::class,Ratings::class],
    views = [CommentWithUser::class], version = 152)

@TypeConverters(RoomConverters::class) //defines type converters to handle non default types
//creates app database singleton
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