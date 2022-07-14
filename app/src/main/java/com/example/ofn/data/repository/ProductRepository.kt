package com.example.ofn.data.repository

import com.example.ofn.data.dao.ProductDao
import com.example.ofn.data.model.Product

class ProductRepository {

    private val productDao = ProductDao()

    fun addStockToProduct(productList: List<Product>) {
        productDao.updateProductStock(productList = productList)
    }

    fun getAllProductsInCategory(categoryName: String, callback: () -> Unit): List<Product> {
        return productDao.getAllProductsInCategory(categoryName, callback)
    }
}