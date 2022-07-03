package com.example.ofn.signup
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignupFormViewModel: ViewModel() {
    private val _name = MutableLiveData<String>("")
    var name: LiveData<String> = _name
    private val _email = MutableLiveData<String>("")
    var email: LiveData<String> = _email
    private val _phone = MutableLiveData<String>("")
    var phone: LiveData<String> = _phone

    fun onNameChange(newText: String){
        _name.value = newText
    }
    fun onEmailChange(newText: String){
        _email.value = newText
    }
    fun onPhoneChange(newText: String){
        _phone.value = newText
    }

}