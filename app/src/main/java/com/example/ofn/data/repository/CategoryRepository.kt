package com.example.ofn.data.repository
import com.example.ofn.data.dao.CategoryDao
import com.example.ofn.data.model.Category


class CategoryRepository(){

    private val categoriesDao = CategoryDao()

    fun addNewCategoryAndProduct(productName: String, categoryName: String, description: String) {
        categoriesDao.postNewCategoryAndProduct(productName, categoryName, description)
    }

    fun getAllCategoriesAndProducts(): List<Category>{
        return categoriesDao.getAllCategories()
    }

    fun renameCategory(categoryName: String, newName: String) {
        categoriesDao.renameCategory(categoryName, newName)
    }

    fun deleteCategory(categoryName: String){
        categoriesDao.deleteCategory(categoryName)
    }

}