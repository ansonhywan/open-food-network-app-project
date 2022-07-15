package com.example.ofn.ui.signup
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ofn.data.Constants
import com.example.ofn.data.repository.AuthRepository
import com.example.ofn.data.repository.UserRepository
import com.example.ofn.ui.login.LoginUIState
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class SignupFormViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val userRepository: UserRepository = UserRepository()
): ViewModel() {

    var signupUIState by mutableStateOf(SignupUIState())

    private var user: FirebaseUser? = authRepository.currentUser

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

    fun signUp(context: Context, email: String, password: String, navigation: ()->Unit)=viewModelScope.launch{
        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(context, "Empty Username or Password!", Toast.LENGTH_SHORT).show()
        }else {
            try {
                authRepository.createUser(email, password) {
                    if (it.containsKey(true)) {
                        user = it[true] as FirebaseUser?
                        userRepository.insert_new_user(user!!.uid, user!!.email!!)
                        Toast.makeText(
                            context,
                            Constants.SIGN_UP_SUCCESS_MESSAGE,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        navigation.invoke()
                    } else {
                        user = null
                        Toast.makeText(context, it[false].toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}