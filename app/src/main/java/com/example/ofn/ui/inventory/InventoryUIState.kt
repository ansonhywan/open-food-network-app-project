package com.example.ofn.ui.inventory

data class InventoryUIState (
    // HashMap<Category Name, HashMap<Product Name, Pair<Stock Available, Add Num>>>
    val categoryUIMap: HashMap<String, HashMap<String, Pair<Int, Int>>> = hashMapOf()
)