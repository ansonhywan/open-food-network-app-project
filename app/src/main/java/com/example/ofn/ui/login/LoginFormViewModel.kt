package com.example.ofn.ui.login
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ofn.data.Constants
import com.example.ofn.data.repository.AuthRepository

class LoginFormViewModel(private val authRepository: AuthRepository = AuthRepository()): ViewModel() {

    var loginUIState by mutableStateOf(LoginUIState())

    fun onUsernameChange(newText: String){
        loginUIState = loginUIState.copy(email = newText)
    }

    fun onPasswordChange(newText: String){
        loginUIState = loginUIState.copy(password = newText)
    }

    fun onRememberMeChange(rememberMe: Boolean){
        loginUIState = loginUIState.copy(rememberMe = rememberMe)
    }

    fun login(context: Context, email: String, password: String, navigation: ()->Unit){
        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(context, Constants.EMPTY_CREDENTIALS_MESSAGE, Toast.LENGTH_SHORT).show()
            return
        }
        authRepository.signInWithEmail(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, Constants.LOGIN_SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show()
                navigation.invoke()
            } else {
                val responseMessage = "Failed with " + task.exception!!.message!!
                Toast.makeText(context, responseMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}