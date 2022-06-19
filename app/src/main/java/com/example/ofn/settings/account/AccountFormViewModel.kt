package com.example.ofn.settings.account

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AccountFormViewModel : ViewModel() {
    var name by mutableStateOf<String>("")
    var email by mutableStateOf<String>("")
    var phone by mutableStateOf<String>("")
    var picture by mutableStateOf<Uri?>(null)

    fun onNameChange(newText: String){
        name = newText
    }
    fun onEmailChange(newText: String){
        email = newText
    }
    fun onPhoneChange(newText: String){
        phone = newText
    }
    fun onPictureChange(newUri: Uri?){
        picture = newUri
    }
}