package com.example.ofn.settings.manage

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ManageViewModel : ViewModel() {
    private val _name = MutableLiveData<String>("")
    var name: LiveData<String> = _name
    private val _category = MutableLiveData<String>("")
    var category: LiveData<String> = _category
    private val _description = MutableLiveData<String>("")
    var description: LiveData<String> = _description

    private val _imageUri = MutableLiveData<Uri?>(null)
    var imageUri: LiveData<Uri?> = _imageUri
    private val _bitmap = MutableLiveData<Bitmap?>(null)
    var bitmap: LiveData<Bitmap?> = _bitmap
    private val _isCameraSelected = MutableLiveData<Boolean>(false)
    var isCameraSelected: LiveData<Boolean> = _isCameraSelected

    fun onNameChange(newText: String){
        _name.value = newText
    }
    fun onCategoryChange(newText: String){
        _category.value = newText
    }
    fun onDescriptionChange(newText: String){
        _description.value = newText
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
    fun onProductSaved():Boolean {
        //save product into databae
        //find avialble id 
        //add information to db
        //return a bool to say if successful
        return true;
    }

    fun resetToDefault(){
        onBitmapChange(null)
        onCameraSelected(false)
        onImageUriChange(null)
        onNameChange("")
        onCategoryChange("")
        onDescriptionChange("")
    }
    fun onProductDelete():Boolean {
        //go into database and delete using the id that is loaded into the model
        return true;
    }

}