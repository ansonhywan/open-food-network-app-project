package com.example.ofn.ui.settings.ManageProductsAndCategories

import androidx.lifecycle.ViewModel
import com.example.ofn.data.repository.CategoryRepository

class ManageProductsAndCategoriesViewModel : ViewModel() {
    var categories = listOf(
        Category(
            name = "Fruits",
            productList = listOf(Product("1","Bananas",1), com.example.ofn.ui.inventory.Product("2","Cherries",0), com.example.ofn.ui.inventory.Product("3","Blueberries",0))
        ),
        Category(
            name = "Vegetables",
            productList = listOf(Product("4","Asparagus",0), com.example.ofn.ui.inventory.Product("5","Avocado",0), com.example.ofn.ui.inventory.Product("6","Broccoli",0))
        ),
        Category(
            name = "Dairy",
            productList = listOf(Product("7","Butter",0), com.example.ofn.ui.inventory.Product("8","Cheese",0), com.example.ofn.ui.inventory.Product("9","Milk",0))
        ),
        Category(
            name = "Meat",
            productList = listOf(Product("10","Bacon",0), com.example.ofn.ui.inventory.Product("11","Beef",0), com.example.ofn.ui.inventory.Product("12","Chicken",0))
        )
    )

    //list of cats
    //list of cats and products
    //do things
    //ugh
    fun getCategories(): List<Category> {
        return categories;
    }

    fun addCategory(categoryName:String) {

    }

    data class Category(val name: String, val productList: List<Product>)
    data class Product(val id: String, val name: String, var amount: Int, var addNum: Int = 0)
}