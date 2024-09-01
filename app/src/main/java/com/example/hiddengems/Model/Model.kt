package com.example.hiddengems.Model

import android.graphics.Bitmap
import android.os.Looper
import android.util.Log
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

    //executes background task of insertCategory query and posting the result back to main thread with same in firebase
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

    //executes background task of insertCity query and posting the result back to main thread  with same in firebase
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

    //executes background task of upsertGem query and posting the result back to main thread  with same in firebase
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

    //executes background task of insertComment query and posting the result back to main thread  with same in firebase
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
    // with same in firebase
    fun deleteGem(gem: Gem, callback: () -> Unit){
        executor.execute{
            getCommentsByGId(gem.gId) { resComments ->
                val comments = resComments as MutableList<Comment>
                for (c in comments){
                    println("look here" +c)
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

    //executes background task of deleteComment query and posting the result back to main thread  with same in firebase
    fun deleteComment(comment: Comment, callback: () -> Unit){
        executor.execute{
            AppLocalDatabase.db.hiddenGemsDao().deleteComment(comment)

            firebaseModel.deleteComments(comment.comId) {
                mainHandler.post {
                    callback()
                }
            }
        }
    }

    //executes background task of deleteRatings query and posting the result back to main thread  with same in firebase
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

    //executes background task of upsertUser query and posting the result back to main thread  with same in firebase
    fun upsertUser(user: User, insertToFirebase:Boolean = true, oldId:Int? = null, callback: () -> Unit){
        executor.execute{
            //AppLocalDatabase.db.hiddenGemsDao().upsertUser(user)
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

    //executes background task of getUserByEmail query and posting the result back to main thread
    fun getUserByEmail(email:String, callback: (User?) -> Unit) {
        executor.execute{
            //Thread.sleep(1000)
            val user = AppLocalDatabase.db.hiddenGemsDao().getUserByEmail(email)
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

    //gets all updated comments from firebase and adds them to room db
    fun getAllCommentsFromFirebase(localLastUpdate:Long,callback: (List<Comment>) -> Unit) {
        firebaseModel.getAllComments(localLastUpdate) { comments ->
            executor.execute {
                println("comments size: " + comments.size.toString())
                if (comments.isNotEmpty()) {
                    for (comment in comments) {
                        insertComment(comment, insertToFirebase = false) {}
                    }
                    mainHandler.post{
                        callback(comments)
                    }
                } else {
                    println("No comments found or there was an error.")
                }
            }
        }
    }

    //gets all updated gems from firebase and adds them to room db
    fun getAllGemsFromFirebase(localLastUpdate:Long, callback: (List<Gem>) -> Unit) {
        firebaseModel.getAllGems(localLastUpdate) { gems ->
            executor.execute {
                println("gems size: " + gems.size.toString())
                if (gems.isNotEmpty()) {
                    for (gem in gems) {
                        upsertGem(gem, insertToFirebase = false) {}
                    }
                    mainHandler.post {
                        callback(gems)
                    }
                }else{
                println("No gems found or there was an error.")
            }
            }
        }
    }

    //gets all updated ratings from firebase and adds them to room db
    fun getAllRatingsFromFirebase(localLastUpdate:Long,callback: (List<Ratings>) -> Unit) {
        firebaseModel.getAllRatings(localLastUpdate) { ratings ->
            executor.execute {
                println("ratings size: " + ratings.size.toString())
                if (ratings.isNotEmpty()) {
                    for (rat in ratings) {
                        upsertRating(rat, insertToFirebase = false) {}
                    }
                    mainHandler.post {
                        callback(ratings)
                    }
                } else {
                    println("No ratings found or there was an error.")
                }
            }
        }
    }

    //gets all updated users from firebase and adds them to room db
    fun getAllUsersFromFirebase(localLastUpdate:Long,callback: (List<User>) -> Unit) {
        firebaseModel.getAllUsers(localLastUpdate) { users ->
            executor.execute {
                println("users size: " + users.size.toString())
                if (users.isNotEmpty()) {
                    for (user in users) {
                        upsertUser(user, insertToFirebase = false) {}
                    }
                    mainHandler.post {
                        callback(users)
                    }
                } else {
                    println("No users found or there was an error.")
                }
            }
        }
    }


    //function does sign up of firebase model, and when its done insers user into room and firebase db with upsert
    fun doSignUp(user: User,email:String,password:String,callback: (Boolean) -> Unit){
        firebaseModel.signUp(email,password) {it->
            if(it != null) {
                upsertUser(user) {
                    callback(true)
                }
            }
            else
            {
                callback(false)
            }
        }
    }

    //function that does log in of firebase model, and when its done, if log in was successful finds the logged in user and passes it, if not, passes null
    fun doLogIn(email: String,password: String,callback: (User?) -> Unit){
        firebaseModel.logIn(email,password){
            if (it == true){
                getUserByEmail(email){user->
                    callback(user)
                }
            }
            else{
                callback(null)
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

        //setting last local updates to 0 for testing
//        Gem.setLastLocalUpdate(0)
//        User.setLastLocalUpdate(0)
//        Comment.setLastLocalUpdate(0)
//        Ratings.setLastLocalUpdate(0)

        //get local last updates
        val gemLocalLastUpdate = Gem.getLocalLastUpdate()
        val commentLocalLastUpdate = Comment.getLocalLastUpdate()
        val userLocalLastUpdate = User.getLocalLastUpdate()
        val ratingsLocalLastUpdate = Ratings.getLocalLastUpdate()

        //get all records since last local update
        getAllGemsFromFirebase(gemLocalLastUpdate) {}
        getAllCommentsFromFirebase(commentLocalLastUpdate) {}
        getAllUsersFromFirebase(userLocalLastUpdate) {}
        getAllRatingsFromFirebase (ratingsLocalLastUpdate){}

        //categories and cities don't change so only get them if they're not in room
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

    }


}