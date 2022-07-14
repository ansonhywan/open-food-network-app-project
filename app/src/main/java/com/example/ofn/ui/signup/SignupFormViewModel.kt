package com.example.ofn.ui.signup
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ofn.data.Constants
import com.example.ofn.data.repository.AuthRepository
import com.example.ofn.data.repository.UserRepository
import com.example.ofn.ui.login.LoginUIState
import com.google.firebase.auth.FirebaseUser

class SignupFormViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val userRepository: UserRepository = UserRepository()
): ViewModel() {

    var signupUIState by mutableStateOf(SignupUIState())

    private var user: FirebaseUser? = authRepository.getCurrentFirebaseUser()

    fun onUsernameChange(newText: String){
        signupUIState = signupUIState.copy(email = newText)
    }
    fun onPasswordChange(newText: String){
        signupUIState = signupUIState.copy(password = newText)
    }
    fun onConfirmPasswordChange(newText: String){
        signupUIState = signupUIState.copy(confirmPassword = newText)
    }
    fun onRememberMeChange(newText: Boolean){
        signupUIState = signupUIState.copy(rememberMe = newText)
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