package com.example.ofn.ui.login
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ofn.data.Constants
import com.example.ofn.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

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

    fun login(context: Context, email: String, password: String, navigation: ()->Unit)= viewModelScope.launch {
        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(context, Constants.EMPTY_CREDENTIALS_MESSAGE, Toast.LENGTH_SHORT).show()
        }else {
            authRepository.signInWithEmail(email, password) {
                if (it.containsKey(true)) {
                    Toast.makeText(context, Constants.LOGIN_SUCCESS_MESSAGE, Toast.LENGTH_SHORT)
                        .show()
                    navigation.invoke()
                } else {
                    Toast.makeText(context, it[false], Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}