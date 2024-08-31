package com.example.hiddengems.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.hiddengems.Model.Category
import com.example.hiddengems.Model.City
import com.example.hiddengems.Model.Comment
import com.example.hiddengems.Model.Gem
import com.example.hiddengems.Model.Ratings
import com.example.hiddengems.Model.User
import com.example.hiddengems.Model.relationships.GemWithComments
import com.example.hiddengems.Model.relationships.GemWithUser
import com.example.hiddengems.Model.relationships.GemWithUserAndComments
import com.example.hiddengems.Model.relationships.UserWithGems

//dao abstration layer to perform database operations
@Dao
interface HiddenGemsDao {

    //getting all categories
    @Query("SELECT * FROM Category")
    fun getAllCategories(): List<Category>

    //adding new category
    @Insert
    fun insertCategory(category: Category)

    //getting all cities
    @Query("SELECT * FROM City")
    fun getAllCities(): List<City>

    //adding new city
    @Insert
    fun insertCity(city: City)

    //getting all gems and the users they were created by
    @Transaction
    @Query("SELECT * FROM Gem")
    fun getAllGems(): List<GemWithUser>

    //getting gems with their users who's ids are in the list of ids
    @Transaction
    @Query("SELECT * FROM Gem WHERE gId IN (:gIds)")
    fun getGemsInIds(gIds: List<Int>): List<GemWithUser>

    //getting the last 10 gems added to db
    @Transaction
    @Query("SELECT * FROM Gem ORDER BY gId DESC LIMIT 10")
    fun getLatestGems(): List<GemWithUser>

    //adds gem if gem doesn't exist, edits gem if it does
    @Upsert
    fun upsertGem(gem:Gem):Long

    //gets gem with its user and comments by its id
    @Transaction
    @Query("SELECT * FROM Gem WHERE gId =:id")
    fun getGemById(id: String): GemWithUserAndComments

    //adds new comment
    @Insert
    fun insertComment(comment: Comment):Long

    //getting all comments
    @Query("SELECT * FROM Comment")
    fun getAllComments(): List<Comment>

    //deletes a gem
    @Delete
    fun deleteGem(gem: Gem)

    //deletes a comment
    @Delete
    fun deleteComment(comment: Comment)

    //deletes a rating
    @Delete
    fun deleteRating(rating: Ratings)

    //gets ratings by gId
    @Query("SELECT * FROM Ratings WHERE gId =:id")
    fun getRatingsByGId(id: String): List<Ratings>

    //gets comments by gId
    @Query("SELECT * FROM Comment WHERE gId =:id")
    fun getCommentsByGId(id: String): List<Comment>

    //filtering and sorting gem by name, type and city if they're not null
    //if rating not null, sorts according to its boolean value
    @Query("""
        SELECT * FROM Gem
        WHERE (:searchString IS NULL OR name LIKE '%' || :searchString || '%')
        AND (:type IS NULL OR type LIKE '%' || :type || '%')
        AND (:city IS NULL OR city LIKE '%' || :city || '%')
        ORDER BY
            CASE WHEN :ratingSort THEN rating END DESC,
            CASE WHEN NOT :ratingSort THEN rating END ASC
    """)
    fun filterAndSortGems(
        searchString: String? = null,
        type: String? = null,
        city: String? = null,
        ratingSort: Boolean? = null
    ): List<GemWithUser>

    //getting all users
    @Query("SELECT * FROM User")
    fun getAllUsers(): List<User>

    //adding new user if doesn't exist, editing if does
    @Upsert
    fun upsertUser(user:User):Long

    //gets user by id
    @Query("SELECT * FROM User WHERE uId =:id")
    fun getUserById(id: String): User

    //gets user by name
    @Query("SELECT * FROM User WHERE user =:name")
    fun getUserByName(name: String): User?

    //gets a user by id and all his gems
    @Transaction
    @Query("SELECT * FROM User WHERE uId=:id")
    fun getGemsOfUser(id:String): UserWithGems

    //getting all ratings
    @Query("SELECT * FROM Ratings")
    fun getAllRatings(): List<Ratings>

    //adding new rating if doesn't exist, editing if does
    @Upsert
    fun upsertRating(rating:Ratings)

    //getting rating by user and gem
    @Query("SELECT * FROM Ratings WHERE gId =:gId AND uId =:uId")
    fun getRatingByGIdAndUId(gId: String, uId:String): Ratings?



}