package com.example.ofn.ui.settings.manage

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ofn.data.repository.CategoryRepository

class ManageViewModel() : ViewModel() {

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

    private val categoryRepo = CategoryRepository()

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
    fun onProductSaved(productName: String, categoryName: String, description: String):Boolean {
        // Add new Product and Category to DB
        categoryRepo.addNewCategoryAndProduct(productName, categoryName, description)

        // TODO: Error checking
        return true
    }

    fun resetToDefault(){
        onBitmapChange(null)
        onCameraSelected(false)
        onImageUriChange(null)
        onNameChange("")
        onCategoryChange("")
        onDescriptionChange("")
    }

    fun onProductDelete(categoryName: String): Boolean {
        categoryRepo.deleteCategory(categoryName)
        return true
    }

    fun renameCategory(categoryName: String, newName: String): Boolean {
        categoryRepo.renameCategory(categoryName, newName)
        return true
    }

}