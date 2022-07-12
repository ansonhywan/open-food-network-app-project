package com.example.ofn.data.repository
import com.example.ofn.data.dao.UserDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User


class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var user: FirebaseUser? = getCurrentFirebaseUser()
    private val userRepository: UserRepository = UserRepository()

    private fun getCurrentFirebaseUser(): FirebaseUser? {
        return this.auth.currentUser
    }

    fun createAccount(email: String, password: String) {
        this.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            task->
            if (task.isSuccessful){
                this.user = task.result.user
                this.userRepository.insert_new_user(this.user!!.uid, this.user!!.email!!)
            }else{
                this.user = null
            }
        }
    }

}