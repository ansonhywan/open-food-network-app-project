package com.example.ofn.settings.manage

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ManageViewModel : ViewModel() {
    var name by mutableStateOf<String>("")
    var picture by mutableStateOf<Uri?>(null)
    var description by mutableStateOf<String>("")
    var category by mutableStateOf<String>("")
    // do something with id, var id by mutableStateOf<Int>()

    fun onNameChange(newText: String){
        name = newText
    }
    fun onPictureChange(newUri: Uri?){
        picture = newUri
    }

    fun onDescriptionChange(newText: String){
        description = newText
    }
    fun onCategoryChange(newText: String){
        category = newText
    }

    fun onProductSaved() {
        //save product into databae
        //return a bool to say if successful

    }

    fun onProductDelete() {
        //go into database and delete using the id that is loaded into the model

    }

}