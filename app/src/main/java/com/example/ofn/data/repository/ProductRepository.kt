package com.example.ofn.data.repository

import com.example.ofn.data.dao.ProductDao
import com.example.ofn.data.model.Product

class ProductRepository {

    private val productDao = ProductDao()

    fun addStockToProduct(productList: List<Product>, newStock: Int) {
        productDao.updateProductStock(productList = productList, newStock = newStock)
    }

    fun getAllProductsInCategory(categoryName: String): List<Product> {
        return productDao.getAllProductsInCategory(categoryName)
    }
}