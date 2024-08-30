package com.example.hiddengems.Model

import android.os.Looper
import androidx.core.os.HandlerCompat
import com.example.hiddengems.Model.relationships.GemWithComments
import com.example.hiddengems.dao.AppLocalDatabase
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.logging.Handler

class Model private constructor() {

    private var mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    //var categories: MutableList<Category> = ArrayList()
    val gems: MutableList<Gem> = ArrayList()
    //val cities: MutableList<String> = ArrayList()
    var currUser: User

    private val executor:Executor = Executors.newSingleThreadExecutor()
    fun getAllCategories(callback: (List<Category>) -> Unit) {
        executor.execute{
            val categories = AppLocalDatabase.db.hiddenGemsDao().getAllCategories()
            mainHandler.post{
                callback(categories)
            }
        }
    }

    private fun insertCategory(category: Category, callback: () -> Unit){
        executor.execute{
            AppLocalDatabase.db.hiddenGemsDao().insertCategory(category)
            mainHandler.post{
                callback()
            }
        }
    }

    fun getAllCities(callback: (List<City>) -> Unit) {
        executor.execute{
            val cities = AppLocalDatabase.db.hiddenGemsDao().getAllCities()
            mainHandler.post{
                callback(cities)
            }
        }
    }

    private fun insertCity(city: City, callback: () -> Unit){
        executor.execute{
            AppLocalDatabase.db.hiddenGemsDao().insertCity(city)
            mainHandler.post{
                callback()
            }
        }
    }

    fun getAllGems(callback: (List<Gem>) -> Unit) {
        executor.execute{
            val gems = AppLocalDatabase.db.hiddenGemsDao().getAllGems()
            mainHandler.post{
                callback(gems)
            }
        }
    }

    fun getLatestGems(callback: (List<Gem>) -> Unit) {
        executor.execute{
            //Thread.sleep(1000)
            val gems = AppLocalDatabase.db.hiddenGemsDao().getLatestGems()
            mainHandler.post{
                callback(gems)
            }
        }
    }

    fun upsertGem(gem: Gem, callback: () -> Unit){
        executor.execute{
            AppLocalDatabase.db.hiddenGemsDao().upsertGem(gem)
            mainHandler.post{
                callback()
            }
        }
    }

    fun getGemById(id:String, callback: (GemWithComments) -> Unit) {
        executor.execute{
            //Thread.sleep(1000)
            val gem = AppLocalDatabase.db.hiddenGemsDao().getGemById(id)
            mainHandler.post{
                callback(gem)
            }
        }
    }

    fun getAllComments(callback: (List<Comment>) -> Unit) {
        executor.execute{
            val comments = AppLocalDatabase.db.hiddenGemsDao().getAllComments()
            mainHandler.post{
                callback(comments)
            }
        }
    }

    fun insertComment(comment: Comment, callback: () -> Unit){
        executor.execute{
            AppLocalDatabase.db.hiddenGemsDao().insertComment(comment)
            mainHandler.post{
                callback()
            }
        }
    }

    fun deleteGem(gem: Gem, callback: () -> Unit){
        executor.execute{
            AppLocalDatabase.db.hiddenGemsDao().deleteGem(gem)
            mainHandler.post{
                callback()
            }
        }
    }

    companion object {
        val instance: Model = Model()
    }

    init {

       // val user:User = User("Billy","Experienced Traveller", mutableListOf(0,2), mutableListOf(1))
        //currUser = user
        val user:User = User()
        currUser = user

//        cities.add("All")
//        cities.add("Paris, FR")
//        cities.add("NYC, USA")
//        cities.add("Rome, IT")
//        cities.add("London, UK")

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
      //  categories = getAllCategories as MutableList<Category>
//        categories.add(Category(0, "All", "ferris_svgrepo_com"))
//        categories.add(Category(1, "Cafe/Restaurant", "restaurant_svgrepo_com"))
//        categories.add(Category(2, "Museum", "museum_svgrepo_com"))
//        categories.add(Category(3, "Park", "park_svgrepo_com"))

        getAllComments { commentsRes ->
            if (commentsRes.isEmpty()){
                insertComment(Comment(1,"Billy", "My favorite cafe")) {}
                insertComment(Comment(1,"Jane", "Thanks for the recommendation")) {}
                insertComment(Comment(1,"Alfy", "Great service!")) {}
                insertComment(Comment(2,"Billy", "What a trip!")) {}
                insertComment(Comment(2,"Jane", "Great museum!")) {}
            }
        }
//        val comments1: MutableList<Comment> = ArrayList()
//        comments1.add(Comment("Billy","My favorite cafe"))
//        comments1.add(Comment("Jane","Thanks for the recommendation"))
//        comments1.add(Comment("Alfy","Great service!"))
       // gems.add(Gem(0,"Billy", "Grande Coffee", "Hidden coffee shop by the park, cozy and homey atmosphere with fantastic pastries!",
       //     "76 rue Leon Dierx","Paris, FR","Cafe/Restaurant", 2.5,0,mutableListOf(2, 3, 3),comments1))

//        val comments2: MutableList<Comment> = ArrayList()
//        comments2.add(Comment("Billy","What a trip!"))
//        comments2.add(Comment("Jane","Great museum!"))
     //   gems.add(Gem(1,"Jane", "Illusion Museum","You won't believe your eyes in this illusion museum!",
      //      "70 Griffin St" ,"NYC, USA","Museum", 3.6,1,mutableListOf(5, 3, 3),comments2))

        //gems.add(Gem(2,"Bobby", "Side Street Park","Beautiful park with pastoral views",
          //  "3 Via Nino Martoglio", "Rome, It","Park", 3.0,-1,mutableListOf( 3, 3)))
        //gems.add(Gem(3,"Billy", "Soup-y", "Tiny restaurant run by a small family, hidden just out of view!",
          //  "174 Quai de Jemmapes","Paris, FR","Cafe/Restaurant",1.0, 0,mutableListOf(1)))

        getAllGems { gemsRes ->
            if (gemsRes.isEmpty()){
                upsertGem(Gem("Billy", "Grande Coffee", "Hidden coffee shop by the park, cozy and homey atmosphere with fantastic pastries!",
                    "76 rue Leon Dierx","Paris, FR","Cafe/Restaurant", 2.5,0,mutableListOf(2, 3, 3))) {}
                upsertGem(Gem("Jane", "Illusion Museum","You won't believe your eyes in this illusion museum!",
                          "70 Griffin St" ,"NYC, USA","Museum", 3.6,1,mutableListOf(5, 3, 3))) {}
                upsertGem(Gem("Bobby", "Side Street Park","Beautiful park with pastoral views",
                      "3 Via Nino Martoglio", "Rome, It","Park", 3.0,-1,mutableListOf( 3, 3))) {}
                upsertGem(Gem("Billy", "Soup-y", "Tiny restaurant run by a small family, hidden just out of view!",
                      "174 Quai de Jemmapes","Paris, FR","Cafe/Restaurant",1.0, 0,mutableListOf(1))) {}
            }
        }
    }


}