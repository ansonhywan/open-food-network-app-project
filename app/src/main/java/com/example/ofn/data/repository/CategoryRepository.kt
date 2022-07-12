package com.example.ofn.data.repository
import com.example.ofn.data.dao.CategoryDao


class CategoryRepository(){

    private val categoriesDao = CategoryDao()

    fun addNewCategoryAndProduct(productName: String, categoryName: String, description: String) {
        categoriesDao.postNewCategoryAndProduct(productName, categoryName, description)
    }

    fun getAllCategoriesAndProducts() {
        categoriesDao.getAllCategories()
    }

}