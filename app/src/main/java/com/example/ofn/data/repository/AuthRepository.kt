package com.example.ofn.data.repository

import com.example.ofn.data.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await

class AuthRepository {

    val currentUser: FirebaseUser? = Firebase.auth.currentUser

    fun hasUser():Boolean{
        return Firebase.auth.currentUser != null
    }

    fun getUserID(): String{
        val currentUser: FirebaseUser? = Firebase.auth.currentUser
        return if (currentUser!=null){
            currentUser!!.uid
        }else{
            ""
        }
    }

    suspend fun createUser(email: String, password: String,
                           onComplete: (HashMap<Boolean, Any>)->Unit)= withContext(Dispatchers.IO){
        Firebase.auth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    onComplete.invoke(hashMapOf<Boolean, Any>(true to it.result.user!!))
                }else{
                    onComplete.invoke(hashMapOf<Boolean, Any>(false to it.exception!!.message!!))
                }
            }.await()
    }

    suspend fun signInWithEmail(email: String, password: String,
                      onComplete: (HashMap<Boolean, String>) -> Unit)= withContext(Dispatchers.IO){
        try {
            Firebase.auth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        onComplete.invoke(hashMapOf(true to Constants.LOGIN_SUCCESS_MESSAGE))
                    } else {
                        onComplete.invoke(hashMapOf(false to it.exception!!.message!!))
                    }
                }.await()
        }catch(e: Exception){
            e.printStackTrace()
        }

    }

}