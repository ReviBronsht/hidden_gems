package com.example.hiddengems.Model

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream

//initiating firebase class with firestore and firebase storage
class FirebaseModel {
    var db:FirebaseFirestore ?= null
    var storage:FirebaseStorage ?= null

    //initializing with firestore and storage
    init {
        db = Firebase.firestore
        storage = Firebase.storage

        //disabling cache and building
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false) // Set to false to disable
            .build()
        db?.firestoreSettings = settings
    }


    //gets all users from firestore
    //after checking that they've been updated after last update user got
    fun getAllUsers(lastLocalUpdate:Long,callback: (List<User>) -> Unit) {
        var lastUser:Long = 0
        var list = mutableListOf<User>()
        db?.collection("users")
           // ?.whereGreaterThan("uId", lastLocalUpdate)
            ?.get()
            ?.addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    var user = User.fromJson(document.data)
                    val time = document.data.get("lastUpdated") as Timestamp
                    println(time.seconds)

                    if (time.seconds > lastLocalUpdate){
                        list.add(user)
                        lastUser = time.seconds}
                }
                if (lastUser > lastLocalUpdate){
                    User.setLastLocalUpdate(lastUser)
                }
                callback(list)
            }
            ?.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                callback(list)
            }
    }

    //gets all ratings from firestore
    //after checking that they've been updated after last update user got
    fun getAllRatings(lastLocalUpdate:Long,callback: (List<Ratings>) -> Unit) {
        var list = mutableListOf<Ratings>()
        var lastRatings:Long = 0
        db?.collection("ratings")
            //?.whereGreaterThan("lastUpdated", lastLocalUpdate)
            ?.get()
            ?.addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    var rats = Ratings.fromJson(document.data)
                    val time = document.data.get("lastUpdated") as Timestamp
                    if (time.seconds > lastLocalUpdate){
                        list.add(rats)
                        lastRatings = time.seconds
                    }
                }
                if (lastRatings > lastLocalUpdate){
                    Ratings.setLastLocalUpdate(lastRatings)
                }
                callback(list)
            }
            ?.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                callback(list)
            }
    }

    //gets all gems from firestore
    //after checking that they've been updated after last update user got
    fun getAllGems(lastLocalUpdate:Long, callback: (List<Gem>) -> Unit) {
        var lastGem:Long = 0
        var list = mutableListOf<Gem>()
        db?.collection("gems")
            //?.whereGreaterThan("gId", lastLocalUpdate)
            ?.get()
            ?.addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    var gem = Gem.fromJson(document.data)
                    val time = document.data.get("lastUpdated") as Timestamp

                    if (time.seconds > lastLocalUpdate){
                        list.add(gem)
                        lastGem = time.seconds}
                }
                if (lastGem > lastLocalUpdate){
                    Gem.setLastLocalUpdate(lastGem)
                }
                callback(list)
            }
            ?.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                callback(list)
            }
    }

    //gets all comments from firestore
    //after checking that they've been updated after last update user got
    fun getAllComments(lastLocalUpdate:Long, callback: (List<Comment>) -> Unit) {
        var lastComment:Long = 0
        var list = mutableListOf<Comment>()
        db?.collection("comments")
           // ?.whereGreaterThan("comId", lastLocalUpdate)
            ?.get()
            ?.addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    var comment = Comment.fromJson(document.data)
                    val time = document.data.get("lastUpdated") as Timestamp
                    if (time.seconds > lastLocalUpdate){
                        list.add(comment)
                        lastComment = time.seconds}
                }
                if (lastComment > lastLocalUpdate) {
                    Comment.setLastLocalUpdate(lastComment)
                }
                callback(list)
            }
            ?.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                callback(list)
            }
    }

    //gets all cities from firestore
    fun getAllCities(callback: (List<City>) -> Unit) {
        var list = mutableListOf<City>()
        db?.collection("cities")
            ?.get()
            ?.addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    var city = City.fromJson(document.data)
                    list.add(city)
                }
                callback(list)
            }
            ?.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                callback(list)
            }
    }
    //gets all categories from firestore
    fun getAllCategories(callback: (List<Category>) -> Unit) {
        var list = mutableListOf<Category>()
        db?.collection("categories")
            ?.get()
            ?.addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    var cat = Category.fromJson(document.data)
                    list.add(cat)
                }
                callback(list)
            }
            ?.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                callback(list)
            }
    }

    //adding user to firestore collection
    fun upsertUser(user: User, callback: () -> Unit) {
        db?.collection("users")?.document(user.uId.toString())?.set(user.toJson())?.addOnCompleteListener{
            callback()
        }
    }

    //adding ratings to firestore collection
    fun upsertRatings(ratings: Ratings, callback: () -> Unit) {
        val documentId = "${ratings.uId}_${ratings.gId}"
        db?.collection("ratings")?.document(documentId)?.set(ratings.toJson())?.addOnCompleteListener{
            callback()
        }
    }

    //adding gem to firestore collection
    fun upsertGem(gem: Gem, callback: () -> Unit) {
        db?.collection("gems")?.document(gem.gId.toString())?.set(gem.toJson())?.addOnCompleteListener{
            callback()
        }
    }

    //adding comment to firestore collection
    fun upsertComment(comment: Comment, callback: () -> Unit) {
        db?.collection("comments")?.document(comment.comId.toString())?.set(comment.toJson())?.addOnCompleteListener{
            callback()
        }
    }

    //adding city to firestore collection
    fun upsertCity(city: City, callback: () -> Unit) {
        db?.collection("cities")?.document(city.name)?.set(city.toJson())?.addOnCompleteListener{
            callback()
        }
    }

    //adding category to firestore collection
    fun upsertCategory(category: Category, callback: () -> Unit) {
        db?.collection("categories")?.document(category.name)?.set(category.toJson())?.addOnCompleteListener{
            callback()
        }
    }

    //deletes gem from firestore collection
    fun deleteGem(gId: Int,callback: () -> Unit){
        db?.collection("gems")?.document(gId.toString())
            ?.delete()
            ?.addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            ?.addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    //deletes ratings from firestore collection
    fun deleteRatings(ratingsId: String,callback: () -> Unit){
        db?.collection("ratings")?.document(ratingsId)
            ?.delete()
            ?.addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            ?.addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    //deletes comments from firestore collection
    fun deleteComments(comId: Int,callback: () -> Unit){
        db?.collection("comments")?.document(comId.toString())
            ?.delete()
            ?.addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            ?.addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }


        //   val db = Firebase.firestore

//    // Create a new user with a first and last name
//    val user = hashMapOf(
//        "first" to "Ada",
//        "last" to "Lovelace",
//        "born" to 1815,
//    )
//
//// Add a new document with a generated ID
//    db.collection("users")
//    .add(user)
//    .addOnSuccessListener { documentReference ->
//        Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//    }
//    .addOnFailureListener { e ->
//        Log.w(ContentValues.TAG, "Error adding document", e)
//    }

    //upload image to firebase storage function
    fun uploadImage(name: String, bitmap: Bitmap, listener: (String?) -> Unit){
        // Create a storage reference from our app
        val storageRef = storage?.reference

        // Create a reference to 'images/name.jpg'
        val imageRef = storageRef?.child( name+".jpg")

        // Get the data from an ImageView as bytes
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = imageRef?.putBytes(data)
        uploadTask?.addOnFailureListener {
            listener(null)
        }?.addOnSuccessListener { taskSnapshot ->
            imageRef?.downloadUrl?.addOnSuccessListener {
                listener(it.toString())
            }
        }

    }

}



