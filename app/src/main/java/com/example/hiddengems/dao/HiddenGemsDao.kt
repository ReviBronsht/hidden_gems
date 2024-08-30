package com.example.hiddengems.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.hiddengems.Model.Category
import com.example.hiddengems.Model.City
import com.example.hiddengems.Model.Comment
import com.example.hiddengems.Model.Gem
import com.example.hiddengems.Model.relationships.GemWithComments

@Dao
interface HiddenGemsDao {

    @Query("SELECT * FROM Category")
    fun getAllCategories(): List<Category>

    @Insert
    fun insertCategory(category: Category)

    @Query("SELECT * FROM City")
    fun getAllCities(): List<City>

    @Insert
    fun insertCity(city: City)

    @Query("SELECT * FROM Gem")
    fun getAllGems(): List<Gem>

    @Query("SELECT * FROM Gem ORDER BY gId DESC LIMIT 10")
    fun getLatestGems(): List<Gem>

    @Upsert
    fun upsertGem(gem:Gem)

    @Query("SELECT * FROM Gem WHERE gId =:id")
    fun getGemById(id: String): GemWithComments

    @Insert
    fun insertComment(comment: Comment)

    @Query("SELECT * FROM Comment")
    fun getAllComments(): List<Comment>

    @Delete
    fun deleteGem(gem: Gem)

}