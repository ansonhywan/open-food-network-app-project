package com.example.ofn.data.dao

import android.util.Log
import androidx.annotation.RestrictTo
import androidx.compose.runtime.rememberCoroutineScope
import com.example.ofn.data.model.Category
import com.example.ofn.data.model.Product
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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
                        val categoryProductCollection = categoriesCollection.document(docId).collection("products")
                        categoryProductCollection
                            .whereEqualTo("productName", productName)
                            .limit(1).get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    if (task.result.isEmpty) {
                                        // Product does not already exist. Add it to sub-collection
                                        categoryProductCollection
                                            .add(newProduct)
                                    } else {
                                        // Product already exists.
                                        // Do nothing.
                                        Log.d("postNewCategoryAndProd", "$productName ALREADY EXISTS in $categoryName")
                                    }
                                }
                            }
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
                        val categoryDocRef = categoriesCollection.document(docId)
                        categoryDocRef
                            .collection("products")
                            .get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    val productDocId = document.id
                                    val productDocRef = categoryDocRef.collection("products").document(productDocId)
                                    productDocRef
                                        .delete()
                                        .addOnSuccessListener { Log.d("deleteCategory", "Successfully deleted product!") }
                                        .addOnFailureListener { e -> Log.w("deleteCategory", "Error deleting product", e) }

                                }
                            }
                        categoryDocRef
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

        fun testGetCategories(): List<Category> {
            val allCategoriesList = mutableListOf<Category>()
            val scope = CoroutineScope(Job() + Dispatchers.Main)
            val categoriesCollection = firestoreDB.collection("categories")
            scope.launch {
                categoriesCollection.get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val productList = mutableListOf<Product>()
                            val categoryName = document.get("categoryName") as String
                            val productsCollectionRef = categoriesCollection.document(document.id).collection("products")
                            productsCollectionRef.get()
                                .addOnSuccessListener { result ->
                                    for (document in result) {
                                        val currentProduct = Product(
                                            productName = document.get("productName") as String,
                                            category = document.get("category") as String,
                                            description = document.get("description") as String,
                                            stock = document.get("stock") as Long,
                                            imageUrl = document.get("imageUrl") as String
                                        )
                                        Log.d("getAllCategories", "Successfully got $currentProduct")
                                        productList.add(currentProduct)
                                        Log.d("getAllCategories", "$allCategoriesList")
                                    }
                                }
                            allCategoriesList.add(Category(categoryName = categoryName, productList = productList))
                            Log.d("getAllCategories", "Successfully got $categoryName")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("getAllCategories", "Error getting documents: ", exception)
                    }
            }

            return allCategoriesList
        }
}

//test