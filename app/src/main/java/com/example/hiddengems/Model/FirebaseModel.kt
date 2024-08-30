package com.example.hiddengems.Model

import android.content.ContentValues
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.firestore

class FirebaseModel {
    var db:FirebaseFirestore ?= null

    init {
        db = Firebase.firestore

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

}



