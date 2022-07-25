package com.example.ofn.data.repository

import android.util.Log
import com.example.ofn.data.dao.ProductDao
import com.example.ofn.data.model.Product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProductRepository {

    private val productDao = ProductDao()
    private val firestoreDB = Firebase.firestore

    fun addStockToProduct(productList: List<Product>) {
        productDao.updateProductStock(productList = productList)
    }

    fun getAllProductsInCategory(categoryName: String, callback: () -> Unit): List<Product> {
        return productDao.getAllProductsInCategory(categoryName, callback)
    }

    fun updateProductDescription(productName: String, description: String) {
        val categoriesCollection = firestoreDB.collection("categories")
        productDao.getProductWithName(categoriesCollection, productName)
            .addOnSuccessListener { result ->
                val productRef = categoriesCollection.document(result.documents[0].id)
                productRef
                    .update("description", description)
                    .addOnSuccessListener { Log.d("updateDescription", "Successfully updated product description: ${productRef.id}") }
                    .addOnFailureListener { e -> Log.w("updateDescription", "Error deleting product", e) }
            }
    }

    fun deleteProduct(productName: String, categoryName: String) {
        val categoriesCollection = firestoreDB.collection("categories")
        productDao.getProductWithName(categoriesCollection, productName)
            .addOnSuccessListener { result ->
                val productRef = categoriesCollection.document(result.documents[0].id)
                productRef
                    .delete()
                    .addOnSuccessListener { Log.d("deleteProduct", "Successfully deleted product: ${productRef.id}") }
                    .addOnFailureListener { e -> Log.w("deleteProduct", "Error deleting product", e) }
            }
    }
}