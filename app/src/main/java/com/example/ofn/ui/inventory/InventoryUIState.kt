package com.example.ofn.ui.inventory

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.ofn.ui.components.SortType

data class InventoryUIState(
    // HashMap<Category Name, HashMap<Product Name, Pair<Stock Available, Add Num>>>
    val categoryUIMap: HashMap<String, HashMap<String, Pair<Int, Int>>> = hashMapOf(),
    val filterList: SnapshotStateList<String> = mutableStateListOf<String>(),
    val sort: MutableState<SortType> = mutableStateOf(SortType.ASC),
    val searchStr: MutableState<String> = mutableStateOf(""),
)