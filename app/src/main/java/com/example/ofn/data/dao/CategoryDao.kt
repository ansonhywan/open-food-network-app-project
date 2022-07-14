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
                    val categoryName = document.get("categoryName") as String
                    allCategoriesList.add(Category(categoryName = categoryName))
                    Log.d("getAllCategories", "Successfully got $categoryName")
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

        categoriesCollection
            .whereEqualTo("categoryName", categoryName)
            .limit(1).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.isEmpty) {
                        // Category does not exist.
                        Log.d("postNewCategoryAndProd", "CATEGORY DOES NOT EXIST")
                        categoriesCollection.add(newCategory)
                            .addOnSuccessListener { docRef ->
                                docRef
                                    .collection("products")
                                    .add(newProduct)
                            }
                    } else {
                        // Category already exists.
                        Log.d("postNewCategoryAndProd", "CATEGORY ALREADY EXISTS")
                        val docId = task.result.documents[0].id
                        categoriesCollection.document(docId)
                            .collection("products")
                            .add(newProduct)
                    }
                }
            }

        // TODO: ERROR CHECKING, CURRENTLY ALWAYS RETURNS TRUE.
        return true;
        }


        fun deleteCategory(categoryName: String) {
            // Delete category from db. This entails deleting all products wtihin it as well.

            val categoriesCollection = firestoreDB.collection("categories")

            categoriesCollection
                .whereEqualTo("categoryName", categoryName)
                .limit(1).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Query successful, category exists.
                        val docId = task.result.documents[0].id
                        categoriesCollection.document(docId)
                            .delete()
                            .addOnSuccessListener { Log.d("deleteCategory", "$categoryName successfully deleted!") }
                            .addOnFailureListener { e -> Log.w("deleteCategory", "Error deleting document", e) }
                    } else {
                        // Category trying to be deleted does not exist.
                    }
                }

        }

        fun renameCategory(categoryName: String, newName: String) {
            val categoriesCollection = firestoreDB.collection("categories")

            categoriesCollection
                .whereEqualTo("categoryName", categoryName)
                .limit(1).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Query successful, category exists.
                        val docId = task.result.documents[0].id
                        categoriesCollection.document(docId)
                            .update("categoryName", newName)
                            .addOnSuccessListener {
                                Log.d(
                                    "renameCategory",
                                    "$categoryName successfully renaned!"
                                )
                            }
                            .addOnFailureListener { e ->
                                Log.w(
                                    "renameCategory",
                                    "Error renaming document",
                                    e
                                )
                            }
                    } else {
                        // Category trying to be renamed does not exist.
                    }

                }
        }
}