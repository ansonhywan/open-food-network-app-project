package com.example.ofn.ui.settings.manage

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ofn.data.repository.CategoryRepository
import com.example.ofn.ui.login.LoginUIState

class ManageViewModel() : ViewModel() {

    var manageUIState by mutableStateOf(ManageUIState())

    private val categoryRepo = CategoryRepository()

    fun onNameChange(newText: String){
        manageUIState = manageUIState.copy(productName = newText)
    }
    fun onCategoryChange(newText: String){
        manageUIState = manageUIState.copy(category = newText)
    }
    fun onDescriptionChange(newText: String){
        manageUIState = manageUIState.copy(description = newText)
    }
    fun onImageUriChange(newUri: Uri?){
        manageUIState = manageUIState.copy(imageUri = newUri)
    }
    fun onBitmapChange(newBitmap: Bitmap?){
        manageUIState = manageUIState.copy(bitmap = newBitmap)
    }
    fun onCameraSelected(cameraSelected: Boolean){
        manageUIState = manageUIState.copy(isCameraSelected = cameraSelected)
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
        categoryRepo.testGetCategories()
//        categoryRepo.deleteCategory(categoryName)
        return true
    }

    fun renameCategory(categoryName: String, newName: String): Boolean {
        categoryRepo.renameCategory(categoryName, newName)
        return true
    }

}