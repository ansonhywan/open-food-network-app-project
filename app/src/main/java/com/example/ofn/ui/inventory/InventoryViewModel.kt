package com.example.ofn.ui.inventory

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ofn.data.model.Product
import com.example.ofn.data.repository.CategoryRepository
import com.example.ofn.data.repository.ProductRepository
import kotlinx.coroutines.launch
import java.math.BigInteger

class InventoryViewModel(
    private val categoryRepo: CategoryRepository = CategoryRepository(),
    private val productRepo: ProductRepository = ProductRepository()
): ViewModel() {

    var inventoryUIState by mutableStateOf(InventoryUIState())

    fun populateCategories(context: Context)=viewModelScope.launch{
        categoryRepo.getAllCategoriesProducts {
            if (it.containsKey(true)){
                val categoryInfo:Pair<String, Pair<String, Int>>  = it[true] as Pair<String, Pair<String, Int>>
                val categoryName: String = categoryInfo.first
                val productName: String = categoryInfo.second.first
                val stock: Int = categoryInfo.second.second
                Log.d("Populate", inventoryUIState.categoryUIMap.toString())
                val newMap: HashMap<String, HashMap<String, Pair<Int, Int>>> = inventoryUIState.categoryUIMap.clone() as HashMap<String, HashMap<String, Pair<Int, Int>>>
                if (inventoryUIState.categoryUIMap.containsKey(categoryName)) {
                    newMap[categoryName]!!.put(productName, Pair(stock, 0))
                }
                else{
                    val newProductEntry: HashMap<String, Pair<Int, Int>> =  hashMapOf(productName to Pair(stock, 0))
                    newMap[categoryName] = newProductEntry
                }
                inventoryUIState  = inventoryUIState.copy(categoryUIMap = newMap)
            }else{
                Toast.makeText(context, "Failed to populate all categories!", Toast.LENGTH_SHORT).show()
            }
        }
    }

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
}
data class ProductInfo(val stock: Long, var addNum: Int)
