package com.example.ofn.settings.manage

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ManageViewModel : ViewModel() {
    val firestoreDB = Firebase.firestore
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

    fun onProductSaved():Boolean {

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

    fun onProductDelete():Boolean {
        //go into database and delete using the id that is loaded into the model
        return true;
    }

}