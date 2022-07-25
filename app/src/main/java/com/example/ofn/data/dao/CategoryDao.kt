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

     fun addNewCategory(newCategory: Category): Task<DocumentReference> {
        return firestoreDB.collection("categories")
            .add(newCategory)
    }

    fun getCategoryWithName(categoryName: String): Task<QuerySnapshot> {
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