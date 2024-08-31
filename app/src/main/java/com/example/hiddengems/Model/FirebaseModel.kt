package com.example.hiddengems.Model

import android.graphics.Bitmap
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

    }
    fun upsertUser(user: User, callback: () -> Unit) {
        db?.collection("users")?.document(user.uId.toString())?.set(user.toJson())?.addOnCompleteListener{
            callback()
    }


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



