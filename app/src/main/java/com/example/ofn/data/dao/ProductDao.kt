package com.example.ofn.data.dao

import android.util.Log
import com.example.ofn.data.model.Product
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProductDao() {

    private val firestoreDB = Firebase.firestore

    fun updateProductStock(productList: List<Product>) {


        val categoriesCollection = firestoreDB.collection("categories")
        productList.forEach{
            categoriesCollection
                .whereEqualTo("categoryName", it.category)
                .limit(1)
                .get()
                .addOnSuccessListener { result ->
                    Log.d("updateProductStock", "$it.stock")
                    val catRef = categoriesCollection.document(result.documents[0].id)
                    catRef
                        .collection("products")
                        .whereEqualTo("productName", it.productName)
                        .limit(1)
                        .get()
                        .addOnSuccessListener { result2 ->
                            val productRef = catRef.collection("products").document(result2.documents[0].id)
                            productRef
                                .update("stock", it.stock)
                                .addOnSuccessListener { Log.d("updateProductStock", "${productRef.id} successfully updated!") }
                                .addOnFailureListener { e -> Log.w("updateProductStock", "Error updating stock", e) }
                        }
                }
        }
    }

    fun updateProductDescription(description: String, productName: String){
        val categoriesCollection = firestoreDB.collection("categories")
    }

    fun getAllProductsInCategory(categoryName: String, callback: () -> Unit): List<Product> {
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
                                    stock = document.get("stock") as Int,
                                    imageUrl = document.get("imageUrl") as String
                                )
                                allProductsList.add(productToAdd)
                                Log.d("getAllProductsInCat", "Successfully got $productToAdd")
                            }
                            callback()
                        }
                        .addOnFailureListener { exception ->
                            Log.d("getAllProductsInCat", "Error getting documents: ", exception)
                        }
                }
            }
        return allProductsList
    }

    fun getProductWithName(collection: CollectionReference, productName: String): Task<QuerySnapshot> {
        return collection
            .whereEqualTo("productName", productName)
            .limit(1).get()
    }
}