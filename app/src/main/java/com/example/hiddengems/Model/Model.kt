package com.example.hiddengems.Model

import android.graphics.Bitmap
import android.os.Looper
import androidx.core.os.HandlerCompat
import com.example.hiddengems.Model.relationships.GemWithUser
import com.example.hiddengems.Model.relationships.GemWithUserAndComments
import com.example.hiddengems.Model.relationships.UserWithGems
import com.example.hiddengems.dao.AppLocalDatabase
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class Model private constructor() {

    val gems: MutableList<Gem> = ArrayList()
    var currUser: User

    //handler associated with the main thread's Looper to post tasks back to the main thread from background threads
    private var mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    // single-threaded worker executor for running tasks in the background without blocking the main thread
    private val executor:Executor = Executors.newSingleThreadExecutor()

    private val firebaseModel = FirebaseModel()


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
    private fun insertCategory(category: Category, insertToFirebase:Boolean = true,callback: () -> Unit){
        executor.execute{
            AppLocalDatabase.db.hiddenGemsDao().insertCategory(category)

            if (insertToFirebase) {
                firebaseModel.upsertCategory(category) {
                    mainHandler.post {
                        callback()
                    }
                }
            }
            else{
                mainHandler.post {
                    callback()
                }
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
    private fun insertCity(city: City, insertToFirebase:Boolean = true, callback: () -> Unit){
        executor.execute{
            AppLocalDatabase.db.hiddenGemsDao().insertCity(city)

            if (insertToFirebase) {
                firebaseModel.upsertCity(city) {
                    mainHandler.post {
                        callback()
                    }
                }
            }
            else{
                mainHandler.post {
                    callback()
                }
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
    fun upsertGem(gem: Gem, insertToFirebase:Boolean = true, oldId:Int? = null,callback: (Int) -> Unit){
        executor.execute{
            val idLong = AppLocalDatabase.db.hiddenGemsDao().upsertGem(gem)
            val idInt = idLong.toInt()
            val updatedGem = gem.copy(gId = idInt)

            if (oldId != null){
                updatedGem.gId = oldId
            }

            if (insertToFirebase) {
                firebaseModel.upsertGem(updatedGem) {
                    mainHandler.post {
                        callback(idInt)
                    }
                }
            }
            else{
                mainHandler.post {
                    callback(idInt)
                }
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
    fun insertComment(comment: Comment, insertToFirebase:Boolean = true, callback: () -> Unit){
        executor.execute{
            val newCommentId = AppLocalDatabase.db.hiddenGemsDao().insertComment(comment)
            val updatedComment = comment.copy(comId = newCommentId.toInt())

            if (insertToFirebase) {
                firebaseModel.upsertComment(updatedComment) {
                    mainHandler.post {
                        callback()
                    }
                }
            }
            else{
                mainHandler.post {
                    callback()
                }
            }
        }
    }

    //executes background task of deleteGem query and posting the result back to main thread, and deleting ratings and comments of gem along with it
    fun deleteGem(gem: Gem, callback: () -> Unit){
        executor.execute{
            getCommentsByGId(gem.gId) { resComments ->
                val comments = resComments as MutableList<Comment>
                for (c in comments){
                    deleteComment(c){
                    }
                }
            }

            getRatingByGId(gem.gId) { resRatings ->
                val ratings = resRatings as MutableList<Ratings>
                for (r in ratings){
                    deleteRatings(r){
                    }
                }
            }

            AppLocalDatabase.db.hiddenGemsDao().deleteGem(gem)

            firebaseModel.deleteGem(gem.gId) {
                mainHandler.post {
                    callback()
                }
            }
        }
    }

    //executes background task of deleteComment query and posting the result back to main thread
    fun deleteComment(comment: Comment, callback: () -> Unit){
        executor.execute{
            AppLocalDatabase.db.hiddenGemsDao().deleteComment(comment)

            firebaseModel.deleteComments(comment.gId) {
                mainHandler.post {
                    callback()
                }
            }
        }
    }

    //executes background task of deleteRatings query and posting the result back to main thread
    fun deleteRatings(ratings: Ratings, callback: () -> Unit){
        executor.execute{
            AppLocalDatabase.db.hiddenGemsDao().deleteRating(ratings)

            firebaseModel.deleteRatings(ratings.uId.toString() + "_" + ratings.gId.toString()) {
                mainHandler.post {
                    callback()
                }
            }
        }
    }

    //executes background task of getCommentsByGId query and posting the result back to main thread
    fun getCommentsByGId(gId:Int, callback: (List<Comment>) -> Unit) {
        executor.execute{
            //Thread.sleep(1000)
            val comments = AppLocalDatabase.db.hiddenGemsDao().getCommentsByGId(gId.toString())
            mainHandler.post{
                callback(comments)
            }
        }
    }

    //executes background task of getRatingByGIdAndUId query and posting the result back to main thread
    fun getRatingByGId(gId:Int, callback: (List<Ratings>) -> Unit) {
        executor.execute{
            //Thread.sleep(1000)
            val ratings = AppLocalDatabase.db.hiddenGemsDao().getRatingsByGId(gId.toString())
            mainHandler.post{
                callback(ratings)
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
    fun upsertUser(user: User, insertToFirebase:Boolean = true, oldId:Int? = null, callback: () -> Unit){
        executor.execute{
            val newUserId = AppLocalDatabase.db.hiddenGemsDao().upsertUser(user)
            val updatedUser = user.copy(uId = newUserId.toInt())

            if (oldId != null){
                updatedUser.uId = oldId
            }

            if (insertToFirebase) {
                firebaseModel.upsertUser(updatedUser) {
                    mainHandler.post {
                        callback()
                    }
                }
            }
            else{
                mainHandler.post {
                    callback()
                }
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

    //executes background task of getAllRatings query and posting the result back to main thread
    fun getAllRatings(callback: (List<Ratings>) -> Unit) {
        executor.execute{
            val ratings = AppLocalDatabase.db.hiddenGemsDao().getAllRatings()
            mainHandler.post{
                callback(ratings)
            }
        }
    }

    //executes background task of upsertRating query and posting the result back to main thread
    fun upsertRating(rating: Ratings, insertToFirebase:Boolean = true, callback: () -> Unit){
        executor.execute{
            AppLocalDatabase.db.hiddenGemsDao().upsertRating(rating)

            if (insertToFirebase) {
                firebaseModel.upsertRatings(rating) {
                    mainHandler.post {
                        callback()
                    }
                }
            }
            else{
                mainHandler.post {
                    callback()
                }
            }
        }
    }

    //executes background task of getRatingByGIdAndUId query and posting the result back to main thread
    fun getRatingByGIdAndUId(gId:String, uId:String, callback: (Ratings?) -> Unit) {
        executor.execute{
            //Thread.sleep(1000)
            val rating = AppLocalDatabase.db.hiddenGemsDao().getRatingByGIdAndUId(gId,uId)
            mainHandler.post{
                callback(rating)
            }
        }
    }

    //gets all categories from firebase and adds them to room db
    fun getAllCategoriesFromFirebase(callback: (List<Category>) -> Unit) {
        firebaseModel.getAllCategories {categories ->
            if (categories.isNotEmpty()){
                for (category in categories){
                    insertCategory(category, insertToFirebase = false) {}
                }
            }
            else{
                println("No categories found or there was an error.")
            }
        }
    }

    //gets all cities from firebase and adds them to room db
    fun getAllCitiesFromFirebase(callback: (List<City>) -> Unit) {
        firebaseModel.getAllCities {cities ->
            if (cities.isNotEmpty()){
                for (city in cities){
                    insertCity(city, insertToFirebase = false) {}
                }
            }
            else{
                println("No cities found or there was an error.")
            }
        }
    }

    //gets all comments from firebase and adds them to room db
    fun getAllCommentsFromFirebase(callback: (List<Comment>) -> Unit) {
        firebaseModel.getAllComments {comments ->
            if (comments.isNotEmpty()){
                for (comment in comments){
                    insertComment(comment, insertToFirebase = false) {}
                }
            }
            else{
                println("No comments found or there was an error.")
            }
        }
    }

    //gets all gems from firebase and adds them to room db
    fun getAllGemsFromFirebase(callback: (List<Gem>) -> Unit) {
        firebaseModel.getAllGems {gems ->
            if (gems.isNotEmpty()){
                for (gem in gems){
                    upsertGem(gem, insertToFirebase = false) {}
                }
            }
            else{
                println("No gems found or there was an error.")
            }
        }
    }

    //gets all ratings from firebase and adds them to room db
    fun getAllRatingsFromFirebase(callback: (List<Ratings>) -> Unit) {
        firebaseModel.getAllRatings {ratings ->
            if (ratings.isNotEmpty()){
                for (rat in ratings){
                    upsertRating(rat, insertToFirebase = false) {}
                }
            }
            else{
                println("No ratings found or there was an error.")
            }
        }
    }

    //gets all users from firebase and adds them to room db
    fun getAllUsersFromFirebase(callback: (List<User>) -> Unit) {
        firebaseModel.getAllUsers {users ->
            if (users.isNotEmpty()){
                for (user in users){
                    upsertUser(user, insertToFirebase = false) {}
                }
            }
            else{
                println("No users found or there was an error.")
            }
        }
    }




        //    implements upload image function from firebase model
    fun uploadImage(name: String, bitmap: Bitmap, listener: (String?) -> Unit){
        firebaseModel.uploadImage(name,bitmap,listener)
    }
        companion object {
        val instance: Model = Model()
    }

    init {


        val user:User = User()
        currUser = user

        //on init - get last update from firebase

        //get all records since last local update
        getAllCategories { categoriesRes ->
            if (categoriesRes.isEmpty()) {
                getAllCategoriesFromFirebase {}
            }
        }
        getAllCities { citiesRes ->
            if (citiesRes.isEmpty()) {
                getAllCitiesFromFirebase {}
            }
        }
        getAllComments { commentsRes ->
            if (commentsRes.isEmpty()) {
                getAllCommentsFromFirebase {}
            }
        }
        getAllGems { gemsRes ->
            if (gemsRes.isEmpty()) {
                getAllGemsFromFirebase {}
            }
        }
        getAllRatings { ratingsRes ->
            if (ratingsRes.isEmpty()) {
                getAllRatingsFromFirebase {}
            }
        }
        getAllUsers { usersRes ->
            if (usersRes.isEmpty()) {
                getAllUsersFromFirebase {}
            }
        }

        //insert new records into room

        //update local last update

        //return complete list from room



        //3. get last update


//        getAllUsers { usersRes->
//            if(usersRes.isEmpty()){
//                upsertUser(User("Billy","Experienced Traveller", mutableListOf(1,3), mutableListOf(2))){}
//                upsertUser(User("Bobby","", image = "https://firebasestorage.googleapis.com/v0/b/hiddengems-f6992.appspot.com/o/images%20(1).jfif?alt=media&token=0da0183d-cbc4-486b-b4a0-b911eab2146d")){}
//                upsertUser(User("Jane","Blogger")){}
//                upsertUser(User("Alfy","", image = "https://firebasestorage.googleapis.com/v0/b/hiddengems-f6992.appspot.com/o/profile-picture.jpeg?alt=media&token=9c993131-659c-48dc-9f0f-4c3d89041e3b")){}
//            }
//        }

//        getAllCities { citiesRes ->
//            if (citiesRes.isEmpty()){
//                insertCity(City("All")) {}
//                insertCity(City("Paris, FR")) {}
//                insertCity(City("NYC, USA")) {}
//                insertCity(City("Rome, IT")) {}
//                insertCity(City("London, UK")) {}
//            }
//        }

//        getAllCategories { categoriesRes ->
//            if (categoriesRes.isEmpty()){
//                insertCategory(Category("All", "ferris_svgrepo_com")) {}
//                insertCategory(Category("Cafe&Restaurant", "restaurant_svgrepo_com")) {}
//                insertCategory(Category("Museum", "museum_svgrepo_com")) {}
//                insertCategory(Category("Park", "park_svgrepo_com")) {}
//            }
//        }


//        getAllComments { commentsRes ->
//            if (commentsRes.isEmpty()){
//                insertComment(Comment(1,1, "My favorite cafe")) {}
//                insertComment(Comment(1,3, "Thanks for the recommendation")) {}
//                insertComment(Comment(1,4, "Great service!")) {}
//                insertComment(Comment(2,1, "What a trip!")) {}
//                insertComment(Comment(2,3, "Great museum!")) {}
//            }
//        }

//        getAllGems { gemsRes ->
//            if (gemsRes.isEmpty()){
//                upsertGem(Gem(1, "Grande Coffee", "Hidden coffee shop by the park, cozy and homey atmosphere with fantastic pastries!",
//                    "76 rue Leon Dierx","Paris, FR","Cafe&Restaurant",
//                    "https://firebasestorage.googleapis.com/v0/b/hiddengems-f6992.appspot.com/o/photo1jpg.jpg?alt=media&token=316752a0-8dae-4f68-87c7-f600efb9f90e"
//                    ,2.5,mutableListOf(2, 3, 3))) {}
//                upsertGem(Gem(3, "Illusion Museum","You won't believe your eyes in this illusion museum!",
//                          "70 Griffin St" ,"NYC, USA","Museum",
//                    "https://firebasestorage.googleapis.com/v0/b/hiddengems-f6992.appspot.com/o/2022-03-09-e-lee-stacy-rangel-stec-philadelphia-museum-of-illusions-opening-tunnel.webp?alt=media&token=935310a5-6be2-466b-8d30-f8a30636ad3a",
//                    3.6,mutableListOf(5, 3, 3))) {}
//                upsertGem(Gem(2, "Side Street Park","Beautiful park with pastoral views",
//                      "3 Via Nino Martoglio", "Rome, It","Park",
//                    "https://firebasestorage.googleapis.com/v0/b/hiddengems-f6992.appspot.com/o/Halleyparknovember_b.jpg?alt=media&token=3c04e9bf-9a3a-4201-b230-07adb2a25aa5"
//                    ,3.0,mutableListOf( 3, 3))) {}
//                upsertGem(Gem(1, "Soup-y", "Tiny restaurant run by a small family, hidden just out of view!",
//                      "174 Quai de Jemmapes","Paris, FR","Cafe&/Restaurant",
//                    "https://firebasestorage.googleapis.com/v0/b/hiddengems-f6992.appspot.com/o/homey-chinese-restaurant.jpg?alt=media&token=bdeb7499-e7db-441c-911d-2e843bdf7eda"
//                    ,1.0,mutableListOf(1))) {}
//            }
//        }

//        getAllRatings { ratingsRes ->
//            if(ratingsRes.isEmpty()){
//                upsertRating(Ratings(1,1,0)){}
//                upsertRating(Ratings(2,1,1)){}
//                upsertRating(Ratings(4,1,0)){}
//            }        }
    }


}