package com.example.ofn.ui.settings.account

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AccountFormViewModel : ViewModel() {
    var accountFormUIState by mutableStateOf(AccountFormUIState())
    fun onNameChange(newText: String){
        accountFormUIState = accountFormUIState.copy(userName = newText)
    }
    fun onEmailChange(newText: String){
        accountFormUIState = accountFormUIState.copy(email = newText)
    }
    fun onPhoneChange(newText: String){
        accountFormUIState = accountFormUIState.copy(phone = newText)
    }
    fun onImageUriChange(newUri: Uri?){
        accountFormUIState = accountFormUIState.copy(imageUri = newUri)
    }
    fun onBitmapChange(newBitmap: Bitmap?){
        accountFormUIState = accountFormUIState.copy(bitmap = newBitmap)
    }
    fun onCameraSelected(cameraSelected: Boolean){
        accountFormUIState = accountFormUIState.copy(isCameraSelected = cameraSelected)
    }
}