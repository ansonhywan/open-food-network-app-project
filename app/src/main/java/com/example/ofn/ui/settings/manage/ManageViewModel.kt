package com.example.ofn.ui.settings.manage

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ofn.data.model.Category
import com.example.ofn.data.repository.CategoryRepository
import com.example.ofn.ui.login.LoginUIState
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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

    fun onProductDelete(context: Context, categoryName: String): Job =viewModelScope.launch{
        categoryRepo.testGetCategories(){
            if (it.containsKey(true)){
                Toast.makeText(context, "Successfully getting all categories!", Toast.LENGTH_SHORT).show()
                val h: List<DocumentSnapshot> = it[true] as List<DocumentSnapshot>
                val t0: Category = h[0].toObject(Category::class.java)!!
                val t1: Category = h[1].toObject(Category::class.java)!!
                val t2: Category = h[2].toObject(Category::class.java)!!
                Log.d("t0", t0.toString()+ t0.productList.toString())
            }else{
                Toast.makeText(context, it[false].toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun renameCategory(categoryName: String, newName: String): Boolean {
        categoryRepo.renameCategory(categoryName, newName)
        return true
    }

}