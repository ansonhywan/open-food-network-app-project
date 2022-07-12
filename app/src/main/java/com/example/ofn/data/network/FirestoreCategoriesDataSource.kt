package com.example.ofn.data.network

import com.google.firebase.firestore.FirebaseFirestore
import com.example.ofn.data.model.Category
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirestoreCategoriesDataSource (
    private val firestoreDB: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getAllCategories(): List<Category> = withContext(ioDispatcher) {
        val snapshot = firestoreDB
            .collection("Categories")
            .get()
            .await()

        snapshot.documents.map { doc ->
            parseCategoryDocument(snapshot = doc)
        }
    }
}