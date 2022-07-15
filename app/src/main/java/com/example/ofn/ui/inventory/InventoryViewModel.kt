package com.example.ofn.ui.inventory

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ofn.data.model.Category
import com.example.ofn.data.model.Product
import com.example.ofn.data.repository.CategoryRepository
import com.example.ofn.data.repository.ProductRepository
import java.math.BigInteger

class InventoryViewModel(): ViewModel() {
    private val categoryRepo: CategoryRepository = CategoryRepository()
    private val productRepo: ProductRepository = ProductRepository()
    val categories: List<Category> = categoryRepo.getAllCategories()
    var addNumMap: HashMap<String, HashMap<String, ProductInfo>> = HashMap()
    var refreshState = mutableStateOf(true)


    fun getProductList(category: String): List<Product> {
        return productRepo.getAllProductsInCategory(category) {
            refreshState.value = false
            refreshState.value = true
        }
    }

    fun onReset() {
        addNumMap.clear()
    }
    fun onSave() {
        var productsToUpdate: MutableList<Product> = mutableListOf()
        addNumMap.toList().forEach { catPair: Pair<String, HashMap<String,ProductInfo>> ->
            catPair.second.toList().forEach {
                productsToUpdate.add(Product(it.first, catPair.first, "", it.second.stock+it.second.addNum, ""))
            }
        }
        addNumMap.clear()
        productRepo.addStockToProduct(productsToUpdate) //todo: this should update the category collection?
        refreshState.value = false
        refreshState.value = true
    }

    fun onProductButtonPress(product: Product, add: Boolean): String {
        if(!addNumMap.containsKey(product.category)) {
            addNumMap.put(product.category, HashMap())
        }
        var curNum: Int? = addNumMap[product.category]?.get(product.productName)?.addNum
        if(add) {
            if(curNum != null && curNum < Int.MAX_VALUE) {
                addNumMap[product.category]?.put(product.productName, ProductInfo(product.stock, curNum + 1))
                return (curNum + 1).toString()
            } else if (curNum == null) {
                addNumMap[product.category]?.put(product.productName, ProductInfo(product.stock, 1))
                return "1"
            }
        } else {
            if(curNum != null && curNum > Int.MIN_VALUE) {
                addNumMap[product.category]?.put(product.productName, ProductInfo(product.stock,curNum - 1))
                return (curNum - 1).toString()
            } else if (curNum == null) {
                addNumMap[product.category]?.put(product.productName, ProductInfo(product.stock, -1))
                return "-1"
            }
        }
        return "0"
    }

    fun onAddNumChange(product: Product, input: String, curInput: String): String {
        // todo: do more verification on the correctness of the input
        val maxInt = BigInteger(Int.MAX_VALUE.toString())

        if(!addNumMap.containsKey(product.category)) {
            addNumMap.put(product.category, HashMap())
        }

        if (input != "" && input != "-" && BigInteger(input) < maxInt) {
            addNumMap[product.category]?.put(product.productName, ProductInfo(product.stock, input.toInt()))
            return input
        }
        return curInput
    }

    data class ProductInfo(val stock: Long, var addNum: Int)
}
