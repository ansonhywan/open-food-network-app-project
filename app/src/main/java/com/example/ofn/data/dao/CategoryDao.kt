package com.example.ofn.data.dao

import android.util.Log
import com.example.ofn.data.model.Category
import com.example.ofn.data.model.Product
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CategoryDao() {

    private val firestoreDB = Firebase.firestore

    fun getAllCategories(): List<Category> {
        val allCategoriesList = mutableListOf<Category>()
        firestoreDB.collection("categories")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    allCategoriesList.add(Category(document.id))
                    Log.d("getAllCategories", "${document.id}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("getAllCategories", "Error getting documents: ", exception)
            }
        return allCategoriesList
    }

    fun postNewCategoryAndProduct(productName: String, categoryName: String, description: String): Boolean {

        val newProduct = Product(
            productName = productName,
            category = categoryName,
            description = description,
            stock = 0,
            imageUrl = "temp"
        )

        // Add category to the categories collection.

        val newCategory = Category(categoryName = categoryName)

        val categoriesCollection = firestoreDB.collection("categories")
            .document(categoryName)

        categoriesCollection.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if(document != null) {
                    if (document.exists()) {
                        Log.d("TAG", "Document already exists.")
                        firestoreDB.collection("categories")
                            .document(categoryName)
                            .collection("products")
                            .document(productName)
                            .set(newProduct)
                    } else {
                        Log.d("TAG", "Document doesn't exist.")
                        categoriesCollection.set(newCategory)
                        firestoreDB.collection("categories")
                            .document(categoryName)
                            .collection("products")
                            .document(productName)
                            .set(newProduct)
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