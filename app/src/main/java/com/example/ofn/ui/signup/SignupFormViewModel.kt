package com.example.ofn.ui.signup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ofn.data.repository.AuthRepository
import kotlinx.coroutines.launch

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

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            authRepository.createAccount(email, password)
        }
    }
}