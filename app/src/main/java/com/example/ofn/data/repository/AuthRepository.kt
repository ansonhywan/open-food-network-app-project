package com.example.ofn.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentFirebaseUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun createAccount(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    fun signInWithEmail(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

}