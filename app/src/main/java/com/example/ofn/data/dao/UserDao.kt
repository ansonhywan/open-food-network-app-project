package com.example.ofn.data.dao

import android.util.Log
import com.example.ofn.data.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class UserDao {
    private val db = Firebase.firestore
    fun addUser(user: User){
        this.db.collection("users").document(user.id).set(user).addOnCompleteListener {
            task->
            if (task.isSuccessful){
                Log.d("UserDao", "Added User with id ${user.id}")
            }else{
                Log.d("UserDao", "Failed to add user")
            }
        }
    }
}