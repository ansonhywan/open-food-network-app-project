package com.example.ofn.data.dao

import android.util.Log
import com.example.ofn.data.model.Category
import com.example.ofn.data.model.Product
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CategoryDao() {

    private val firestoreDB = Firebase.firestore

    fun getAllCategories(): Task<QuerySnapshot> {
        return firestoreDB.collection("categories").get()
    }

    fun getProductsUnderCategory(categoryId: String): Task<QuerySnapshot> {
        return firestoreDB.collection("categories")
            .document(categoryId).collection("products").get()
    }

    suspend fun postNewCategoryAndProduct(productName: String, categoryName: String, description: String): Boolean {

        val newProduct = Product(
            productName = productName,
            category = categoryName,
            description = description,
            stock = 0,
            imageUrl = "temp"
        )

        // Add category to the categories collection.

        val newCategory = Category(categoryName = categoryName, productList = listOf())

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

    suspend fun deleteCategory(categoryName: String): CollectionReference {
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
        return categoriesCollection
        }

    suspend fun renameCategory(categoryName: String, newName: String): CollectionReference {
        val categoriesCollection = firestoreDB.collection("categories")

        return categoriesCollection

    }
     fun addNewCategory(newCategory: Category): Task<DocumentReference> {
        return firestoreDB.collection("categories")
            .add(newCategory)
    }

    suspend fun getCategoryWithName(categoryName: String): Task<QuerySnapshot> {
        return firestoreDB.collection("categories")
            .whereEqualTo("categoryName", categoryName)
            .limit(1) // Limit 1 because there should only be one category with a given name.
            .get()
    }

    fun getCategoryWithId(id: String): DocumentReference {
        return firestoreDB.collection("categories")
            .document(id)
    }

}