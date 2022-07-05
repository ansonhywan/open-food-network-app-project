package com.example.ofn.presentation.signup
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignupFormViewModel: ViewModel() {
    private val _username = MutableLiveData<String>("")
    var username: LiveData<String> = _username
    private val _password = MutableLiveData<String>("")
    var password: LiveData<String> = _password
    private val _confirm_password = MutableLiveData<String>("")
    var confirm_password: LiveData<String> = _confirm_password
    private val _rememberMe = MutableLiveData<Boolean>(false)
    var rememberMe: LiveData<Boolean> = _rememberMe
    fun onUsernameChange(newText: String){
        _username.value = newText
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
}