package com.example.ofn.ui.settings.manage

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ofn.ui.inventory.firestoreDB
import com.google.firebase.firestore.FieldValue

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
    fun onProductSaved(name: String, category: String, description: String):Boolean {
        // Add product to the inventory collection.
        val inventoryCollection = firestoreDB.collection("inventory")

        val newProductFields = hashMapOf(
            "category" to category,
            "description" to description,
            "stock" to 0
        )

        inventoryCollection.document(name)
            .set(newProductFields)


        // Add category to the categories collection.
        val categoriesCollection = firestoreDB.collection("categories").document(category)

        val newCategoryFields = hashMapOf(
            "products" to arrayListOf(name)
        )

        categoriesCollection.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if(document != null) {
                    if (document.exists()) {
                        Log.d("TAG", "Document already exists.")
                        categoriesCollection.update("products", FieldValue.arrayUnion(name))
                    } else {
                        Log.d("TAG", "Document doesn't exist.")
                        categoriesCollection.set(newCategoryFields)
                    }
                }
            } else {
                Log.d("TAG", "Error: ", task.exception)
            }
        }

        // TODO: ERROR CHECKING, CURRENTLY ALWAYS RETURNS TRUE.
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