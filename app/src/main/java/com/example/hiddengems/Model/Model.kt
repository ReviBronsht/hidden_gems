package com.example.hiddengems.Model

import android.os.Looper
import androidx.core.os.HandlerCompat
import com.example.hiddengems.Model.relationships.GemWithComments
import com.example.hiddengems.Model.relationships.GemWithUser
import com.example.hiddengems.Model.relationships.GemWithUserAndComments
import com.example.hiddengems.Model.relationships.UserWithGems
import com.example.hiddengems.dao.AppLocalDatabase
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.logging.Handler

class Model private constructor() {

    val gems: MutableList<Gem> = ArrayList()
    var currUser: User

    //handler associated with the main thread's Looper to post tasks back to the main thread from background threads
    private var mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    // single-threaded worker executor for running tasks in the background without blocking the main thread
    private val executor:Executor = Executors.newSingleThreadExecutor()


    //executes background task of getAllCategories query and posting the result back to main thread
    fun getAllCategories(callback: (List<Category>) -> Unit) {
        executor.execute{
            val categories = AppLocalDatabase.db.hiddenGemsDao().getAllCategories()
            mainHandler.post{
                callback(categories)
            }
        }
    }

    //executes background task of insertCategory query and posting the result back to main thread
    private fun insertCategory(category: Category, callback: () -> Unit){
        executor.execute{
            AppLocalDatabase.db.hiddenGemsDao().insertCategory(category)
            mainHandler.post{
                callback()
            }
        }
    }

    //executes background task of getAllCities query and posting the result back to main thread
    fun getAllCities(callback: (List<City>) -> Unit) {
        executor.execute{
            val cities = AppLocalDatabase.db.hiddenGemsDao().getAllCities()
            mainHandler.post{
                callback(cities)
            }
        }
    }

    //executes background task of insertCity query and posting the result back to main thread
    private fun insertCity(city: City, callback: () -> Unit){
        executor.execute{
            AppLocalDatabase.db.hiddenGemsDao().insertCity(city)
            mainHandler.post{
                callback()
            }
        }
    }

    //executes background task of getAllGems query and posting the result back to main thread
    fun getAllGems(callback: (List<GemWithUser>) -> Unit) {
        executor.execute{
            val gems = AppLocalDatabase.db.hiddenGemsDao().getAllGems()
            mainHandler.post{
                callback(gems)
            }
        }
    }

    //executes background task of getLatestGems query and posting the result back to main thread
    fun getLatestGems(callback: (List<GemWithUser>) -> Unit) {
        executor.execute{
            //Thread.sleep(1000)
            val gems = AppLocalDatabase.db.hiddenGemsDao().getLatestGems()
            mainHandler.post{
                callback(gems)
            }
        }
    }

    //executes background task of upsertGem query and posting the result back to main thread
    fun upsertGem(gem: Gem, callback: () -> Unit){
        executor.execute{
            AppLocalDatabase.db.hiddenGemsDao().upsertGem(gem)
            mainHandler.post{
                callback()
            }
        }
    }

    //executes background task of getGemById query and posting the result back to main thread
    fun getGemById(id:String, callback: (GemWithUserAndComments) -> Unit) {
        executor.execute{
            //Thread.sleep(1000)
            val gem = AppLocalDatabase.db.hiddenGemsDao().getGemById(id)
            mainHandler.post{
                callback(gem)
            }
        }
    }

    //executes background task of getAllComments query and posting the result back to main thread
    fun getAllComments(callback: (List<Comment>) -> Unit) {
        executor.execute{
            val comments = AppLocalDatabase.db.hiddenGemsDao().getAllComments()
            mainHandler.post{
                callback(comments)
            }
        }
    }

    //executes background task of getGemsInIds query and posting the result back to main thread
    fun getGemsInIds(ids:MutableList<Int>,callback: (List<GemWithUser>) -> Unit) {
        executor.execute{
            val gems = AppLocalDatabase.db.hiddenGemsDao().getGemsInIds(ids)
            mainHandler.post{
                callback(gems)
            }
        }
    }

    //executes background task of insertComment query and posting the result back to main thread
    fun insertComment(comment: Comment, callback: () -> Unit){
        executor.execute{
            AppLocalDatabase.db.hiddenGemsDao().insertComment(comment)
            mainHandler.post{
                callback()
            }
        }
    }

    //executes background task of deleteGem query and posting the result back to main thread
    fun deleteGem(gem: Gem, callback: () -> Unit){
        executor.execute{
            AppLocalDatabase.db.hiddenGemsDao().deleteGem(gem)
            mainHandler.post{
                callback()
            }
        }
    }

    //executes background task of filterAndSortGems query and posting the result back to main thread
    fun filterAndSortGems(searchString:String?, type:String?, city:String?, ratingSort:Boolean?, callback: (List<GemWithUser>) -> Unit) {
        executor.execute{
            val gems = AppLocalDatabase.db.hiddenGemsDao().filterAndSortGems(searchString, type, city, ratingSort)
            mainHandler.post{
                callback(gems)
            }
        }
    }

    //executes background task of getAllUsers query and posting the result back to main thread
    fun getAllUsers(callback: (List<User>) -> Unit) {
        executor.execute{
            val user = AppLocalDatabase.db.hiddenGemsDao().getAllUsers()
            mainHandler.post{
                callback(user)
            }
        }
    }

    //executes background task of upsertUser query and posting the result back to main thread
    fun upsertUser(user: User, callback: () -> Unit){
        executor.execute{
            AppLocalDatabase.db.hiddenGemsDao().upsertUser(user)
            mainHandler.post{
                callback()
            }
        }
    }

    //executes background task of getUserById query and posting the result back to main thread
    fun getUserById(id:String, callback: (User) -> Unit) {
        executor.execute{
            //Thread.sleep(1000)
            val user = AppLocalDatabase.db.hiddenGemsDao().getUserById(id)
            mainHandler.post{
                callback(user)
            }
        }
    }

    //executes background task of getUserByName query and posting the result back to main thread
    fun getUserByName(name:String, callback: (User?) -> Unit) {
        executor.execute{
            //Thread.sleep(1000)
            val user = AppLocalDatabase.db.hiddenGemsDao().getUserByName(name)
            mainHandler.post{
                callback(user)
            }
        }
    }

    //executes background task of getGemsOfUser query and posting the result back to main thread
    fun getGemsOfUser(id:String, callback: (UserWithGems) -> Unit) {
        executor.execute{
            val userWithGems = AppLocalDatabase.db.hiddenGemsDao().getGemsOfUser(id)
            mainHandler.post{
                callback(userWithGems)
            }
        }
    }

    companion object {
        val instance: Model = Model()
    }

    init {


        val user:User = User()
        currUser = user


        getAllUsers { usersRes->
            if(usersRes.isEmpty()){
                upsertUser(User("Billy","Experienced Traveller", mutableListOf(1,3), mutableListOf(2))){}
                upsertUser(User("Bobby","")){}
                upsertUser(User("Jane","Blogger")){}
                upsertUser(User("Alfy","")){}
            }
        }

        getAllCities { citiesRes ->
            if (citiesRes.isEmpty()){
                insertCity(City("All")) {}
                insertCity(City("Paris, FR")) {}
                insertCity(City("NYC, USA")) {}
                insertCity(City("Rome, IT")) {}
                insertCity(City("London, UK")) {}
            }
        }

        getAllCategories { categoriesRes ->
            if (categoriesRes.isEmpty()){
                insertCategory(Category("All", "ferris_svgrepo_com")) {}
                insertCategory(Category("Cafe/Restaurant", "restaurant_svgrepo_com")) {}
                insertCategory(Category("Museum", "museum_svgrepo_com")) {}
                insertCategory(Category("Park", "park_svgrepo_com")) {}
            }
        }

        getAllComments { commentsRes ->
            if (commentsRes.isEmpty()){
                insertComment(Comment(1,1, "My favorite cafe")) {}
                insertComment(Comment(1,3, "Thanks for the recommendation")) {}
                insertComment(Comment(1,4, "Great service!")) {}
                insertComment(Comment(2,1, "What a trip!")) {}
                insertComment(Comment(2,3, "Great museum!")) {}
            }
        }

        getAllGems { gemsRes ->
            if (gemsRes.isEmpty()){
                upsertGem(Gem(1, "Grande Coffee", "Hidden coffee shop by the park, cozy and homey atmosphere with fantastic pastries!",
                    "76 rue Leon Dierx","Paris, FR","Cafe/Restaurant", 2.5,0,mutableListOf(2, 3, 3))) {}
                upsertGem(Gem(3, "Illusion Museum","You won't believe your eyes in this illusion museum!",
                          "70 Griffin St" ,"NYC, USA","Museum", 3.6,1,mutableListOf(5, 3, 3))) {}
                upsertGem(Gem(2, "Side Street Park","Beautiful park with pastoral views",
                      "3 Via Nino Martoglio", "Rome, It","Park", 3.0,-1,mutableListOf( 3, 3))) {}
                upsertGem(Gem(1, "Soup-y", "Tiny restaurant run by a small family, hidden just out of view!",
                      "174 Quai de Jemmapes","Paris, FR","Cafe/Restaurant",1.0, 0,mutableListOf(1))) {}
            }
        }
    }


}