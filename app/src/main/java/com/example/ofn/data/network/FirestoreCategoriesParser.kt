package com.example.ofn.data.network

import com.example.ofn.data.model.Category
import com.google.firebase.firestore.DocumentSnapshot

fun parseCategoryDocument(
    snapshot: DocumentSnapshot
): Category = Category (
    category = snapshot.id,
    productList = snapshot.get("products") as List<String>
)