package com.example.ofn.settings.ManageProductsAndCategories

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ofn.inventory.Category
import com.example.ofn.inventory.Produce

class ManageProductsAndCategoriesViewModel : ViewModel() {
    var categories = listOf(
        Category(
            name = "Category 1",
            produceList = listOf(com.example.ofn.inventory.Produce("1","Produce 0",1), com.example.ofn.inventory.Produce("12","Produce 0",0), com.example.ofn.inventory.Produce("13","Produce 0",0))
        ),
        Category(
            name = "Category 2",
            produceList = listOf(com.example.ofn.inventory.Produce("2","Produce 0",0), com.example.ofn.inventory.Produce("3","Produce 0",0), com.example.ofn.inventory.Produce("4","Produce 0",0))
        ),
        Category(
            name = "Category 3",
            produceList = listOf(com.example.ofn.inventory.Produce("5","Produce 0",0), com.example.ofn.inventory.Produce("6","Produce 0",0), com.example.ofn.inventory.Produce("7","Produce 0",0), com.example.ofn.inventory.Produce("19","Produce 0",0))
        ),
        Category(
            name = "Category 4",
            produceList = listOf(com.example.ofn.inventory.Produce("14","Produce 0",0), com.example.ofn.inventory.Produce("15","Produce 0",0), com.example.ofn.inventory.Produce("16","Produce 0",0))
        ),
        Category(
            name = "Category 5",
            produceList = listOf(com.example.ofn.inventory.Produce("17","Produce 0",0), com.example.ofn.inventory.Produce("18","Produce 0",0), com.example.ofn.inventory.Produce("19","Produce 0",0))
        )
    )

    //list of cats
    //list of cats and products
    //do things
    //ugh

    @JvmName("getCategories1")
    fun getCategories(): List<com.example.ofn.inventory.Category> {
        return categories;
    }

    fun addCategory(categoryName:String) {

    }

    data class Category(val name: String, val produceList: List<Produce>)
    data class Produce(val id: String, val name: String, var amount: Int, var addNum: Int = 0)
}