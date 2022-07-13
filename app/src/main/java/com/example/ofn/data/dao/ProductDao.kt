package com.example.ofn.data.dao

import android.util.Log
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


}