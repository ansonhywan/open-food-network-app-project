package com.example.ofn.ui.signup
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ofn.data.Constants
import com.example.ofn.data.repository.AuthRepository
import com.example.ofn.data.repository.UserRepository
import com.google.firebase.auth.FirebaseUser

class SignupFormViewModel: ViewModel() {
    private val _email = MutableLiveData<String>("")
    var email: LiveData<String> = _email
    private val _password = MutableLiveData<String>("")
    var password: LiveData<String> = _password
    private val _confirm_password = MutableLiveData<String>("")
    var confirm_password: LiveData<String> = _confirm_password
    private val _rememberMe = MutableLiveData<Boolean>(false)
    var rememberMe: LiveData<Boolean> = _rememberMe

    private val authRepository: AuthRepository = AuthRepository()
    private var user: FirebaseUser? = authRepository.getCurrentFirebaseUser()
    private val userRepository: UserRepository = UserRepository()

    fun onUsernameChange(newText: String){
        _email.value = newText
    }
    fun onPasswordChange(newText: String){
        _password.value = newText
    }
    fun onConfirmPasswordChange(newText: String){
        _confirm_password.value = newText
    }
    fun onRememberMeChange(newText: Boolean){
        _rememberMe.value = newText
    }

    fun signUp(context: Context, email: String, password: String, navigation: ()->Unit){
        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(context, "Empty Username or Password!", Toast.LENGTH_SHORT).show()
            return
        }
        authRepository.createAccount(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                user = task.result.user
                userRepository.insert_new_user(user!!.uid, user!!.email!!)
                Toast.makeText(context, Constants.SIGN_UP_SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show()
                navigation.invoke()
            } else {
                user = null
                val responseMessage = "Failed with " + task.exception!!.message!!
                Toast.makeText(context, responseMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}