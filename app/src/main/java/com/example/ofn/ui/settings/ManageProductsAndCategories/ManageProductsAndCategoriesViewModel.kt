package com.example.ofn.ui.settings.ManageProductsAndCategories

import androidx.lifecycle.ViewModel
import com.example.ofn.data.model.Category
import com.example.ofn.data.model.Product
import com.example.ofn.data.repository.CategoryRepository

class ManageProductsAndCategoriesViewModel(private val categoryRepository: CategoryRepository = CategoryRepository()) : ViewModel() {

    fun getCategories(): List<Category> {
        return categoryRepository.getAllCategories()
    }
}