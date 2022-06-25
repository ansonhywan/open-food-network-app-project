package com.example.ofn.settings.account

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AccountFormViewModel : ViewModel() {
    private val _name = MutableLiveData<String>("")
    var name: LiveData<String> = _name
    private val _email = MutableLiveData<String>("")
    var email: LiveData<String> = _email
    private val _phone = MutableLiveData<String>("")
    var phone: LiveData<String> = _phone
    private val _imageUri = MutableLiveData<Uri?>(null)
    var imageUri: LiveData<Uri?> = _imageUri
    private val _bitmap = MutableLiveData<Bitmap?>(null)
    var bitmap: LiveData<Bitmap?> = _bitmap
    private val _isCameraSelected = MutableLiveData<Boolean>(false)
    var isCameraSelected: LiveData<Boolean> = _isCameraSelected
    fun onNameChange(newText: String){
        _name.value = newText
    }
    fun onEmailChange(newText: String){
        _email.value = newText
    }
    fun onPhoneChange(newText: String){
        _phone.value = newText
    }
    fun onImageUriChange(newUri: Uri?){
        _imageUri.value = newUri
    }
    fun onBitmapChange(newBitmap: Bitmap?){
        _bitmap.value = newBitmap
    }
    fun onCameraSelected(cameraSelected: Boolean){
        _isCameraSelected.value = cameraSelected
    }
}