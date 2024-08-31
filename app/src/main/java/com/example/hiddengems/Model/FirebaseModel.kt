package com.example.hiddengems.Model

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream

class FirebaseModel {
    var db:FirebaseFirestore ?= null
    var storage:FirebaseStorage ?= null

    init {
        db = Firebase.firestore
        storage = Firebase.storage

        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false) // Set to false to disable
            .build()
        db?.firestoreSettings = settings
    }


    fun getAllUsers(callback: (List<User>) -> Unit) {
        var list = mutableListOf<User>()
        db?.collection("users")
            ?.get()
            ?.addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    var user = User.fromJson(document.data)
                    list.add(user)
                }
                callback(list)
            }
            ?.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                callback(list)
            }
    }

    fun getAllRatings(callback: (List<Ratings>) -> Unit) {
        var list = mutableListOf<Ratings>()
        db?.collection("ratings")
            ?.get()
            ?.addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    var rats = Ratings.fromJson(document.data)
                    list.add(rats)
                }
                callback(list)
            }
            ?.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                callback(list)
            }
    }

    fun getAllGems(callback: (List<Gem>) -> Unit) {
        var list = mutableListOf<Gem>()
        db?.collection("gems")
            ?.get()
            ?.addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    var gem = Gem.fromJson(document.data)
                    list.add(gem)
                }
                callback(list)
            }
            ?.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                callback(list)
            }
    }

    fun getAllComments(callback: (List<Comment>) -> Unit) {
        var list = mutableListOf<Comment>()
        db?.collection("comments")
            ?.get()
            ?.addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    var comment = Comment.fromJson(document.data)
                    list.add(comment)
                }
                callback(list)
            }
            ?.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                callback(list)
            }
    }

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

    fun upsertUser(user: User, callback: () -> Unit) {
        db?.collection("users")?.document(user.uId.toString())?.set(user.toJson())?.addOnCompleteListener{
            callback()
        }
    }

    fun upsertRatings(ratings: Ratings, callback: () -> Unit) {
        val documentId = "${ratings.uId}_${ratings.gId}"
        db?.collection("ratings")?.document(documentId)?.set(ratings.toJson())?.addOnCompleteListener{
            callback()
        }
    }

    fun upsertGem(gem: Gem, callback: () -> Unit) {
        db?.collection("gems")?.document(gem.gId.toString())?.set(gem.toJson())?.addOnCompleteListener{
            callback()
        }
    }

    fun upsertComment(comment: Comment, callback: () -> Unit) {
        db?.collection("comments")?.document(comment.comId.toString())?.set(comment.toJson())?.addOnCompleteListener{
            callback()
        }
    }

    fun upsertCity(city: City, callback: () -> Unit) {
        db?.collection("cities")?.document(city.name)?.set(city.toJson())?.addOnCompleteListener{
            callback()
        }
    }

    fun upsertCategory(category: Category, callback: () -> Unit) {
        db?.collection("categories")?.document(category.name)?.set(category.toJson())?.addOnCompleteListener{
            callback()
        }
    }

    fun deleteGem(gId: Int,callback: () -> Unit){
        db?.collection("gems")?.document(gId.toString())
            ?.delete()
            ?.addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            ?.addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    fun deleteRatings(ratingsId: String,callback: () -> Unit){
        db?.collection("ratings")?.document(ratingsId)
            ?.delete()
            ?.addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            ?.addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

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



