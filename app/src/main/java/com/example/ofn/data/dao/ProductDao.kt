package com.example.ofn.data.dao

import android.util.Log
import com.example.ofn.data.model.Category
import com.example.ofn.data.model.Product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProductDao() {

    private val firestoreDB = Firebase.firestore

    fun updateProductStock(productList: List<Product>, newStock: Int) {
        // Should update since if there is a product in the Inventory Page, it is already in the DB.
        productList.forEach{
            Log.i("updateProductStock", "${it.productName}: ${it.stock} => $newStock")
            firestoreDB.collection("inventory").document(it.productName)
                .update("stock", newStock)
                .addOnSuccessListener { Log.d("updateProductStock", "Stock successfully updated!") }
                .addOnFailureListener { e -> Log.w("updateProductStock", "Error updating stock", e) }
        }
    }

    fun getAllProductsInCategory(categoryName: String): List<Product> {
        val allProductsList = mutableListOf<Product>()

        val categoriesCollection = firestoreDB.collection("categories")

        categoriesCollection
            .whereEqualTo("categoryName", categoryName)
            .limit(1).get()
            .addOnCompleteListener { task->
                if (task.isSuccessful) {
                    // Query successful, category exists.
                    val docId = task.result.documents[0].id
                    categoriesCollection.document(docId)
                        .collection("products")
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                val productToAdd = Product(
                                    productName = document.get("productName") as String,
                                    category = document.get("category") as String,
                                    description = document.get("description") as String,
                                    stock = document.get("stock") as Long,
                                    imageUrl = document.get("imageUrl") as String
                                )
                                allProductsList.add(productToAdd)
                                Log.d("getAllProductsInCat", "Successfully got $productToAdd")
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d("getAllProductsInCat", "Error getting documents: ", exception)
                        }
                }
            }
        return allProductsList
    }
}