package com.example.ofn.data.dao

import android.util.Log
import com.example.ofn.data.model.Category
import com.example.ofn.data.model.Product
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CategoryDao() {

    private val firestoreDB = Firebase.firestore

    fun getAllCategories(): Map<String, Any?> {
        val collectionsProductListMap = mutableMapOf<String, Any?>()
        firestoreDB.collection("categories")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    collectionsProductListMap[document.id] =
                        document.get("productList")
                    Log.d("getAllCategories", "${document.id} => ${document.get("productList")}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("getAllCategories", "Error getting documents: ", exception)
            }
        for ((key, value) in collectionsProductListMap) {
            Log.d("getAllCategories", "$key => $value")
        }
        return collectionsProductListMap
    }

    fun postNewCategoryAndProduct(productName: String, categoryName: String, description: String): Boolean {

        // Add product to the inventory collection.
        val inventoryCollection = firestoreDB.collection("inventory")

        val newProduct = Product(productName = productName,category = categoryName, description = description, stock = 0)

        inventoryCollection.document(productName)
            .set(newProduct)


        // Add category to the categories collection.

        val newCategory = Category(categoryName = categoryName, productList = listOf(productName))
        val categoriesCollection = firestoreDB.collection("categories").document(categoryName)

        categoriesCollection.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if(document != null) {
                    if (document.exists()) {
                        Log.d("TAG", "Document already exists.")
                        categoriesCollection.update("productList", FieldValue.arrayUnion(productName))
                    } else {
                        Log.d("TAG", "Document doesn't exist.")
                        categoriesCollection.set(newCategory)
                    }
                }
            } else {
                Log.d("TAG", "Error: ", task.exception)
            }
        }

        // TODO: ERROR CHECKING, CURRENTLY ALWAYS RETURNS TRUE.
        return true;
        }


}