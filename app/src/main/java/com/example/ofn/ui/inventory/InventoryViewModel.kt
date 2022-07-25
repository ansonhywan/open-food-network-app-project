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

    fun populateCategories()=viewModelScope.launch{
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
                Log.d("Populate", "Failed to populate all categories")
            }
        }
    }

    fun resetAllAddNum(save: Boolean): Boolean {
        val categoryMap:HashMap<String, HashMap<String, Pair<Int, Int>>> = inventoryUIState.categoryUIMap
        var newMap:HashMap<String, HashMap<String, Pair<Int, Int>>> = categoryMap.clone() as HashMap<String, HashMap<String, Pair<Int, Int>>>
        for (categoryName:String in newMap.keys){
            val products: HashMap<String, Pair<Int, Int>> = newMap[categoryName]!!
            for (productName in products.keys){
                var stock = products[productName]!!.first
                if(save) {
                    stock += products[productName]!!.second
                }
                if (stock < 0 ){
                    return false
                }
                products[productName] = Pair(stock, 0)

            }
        }
        inventoryUIState  = inventoryUIState.copy(categoryUIMap = newMap)
        return true
    }
    fun onSave(context: Context) {
        var productsToUpdate: MutableList<Product> = mutableListOf()
        val categoryMap:HashMap<String, HashMap<String, Pair<Int, Int>>> = inventoryUIState.categoryUIMap
        if (resetAllAddNum(true) == false){
            Toast.makeText(context, "Remove stock with invalid amount, please try again!", Toast.LENGTH_SHORT).show()
            return
        }
        categoryMap.toList().forEach { catPair ->
            catPair.second.toList().forEach {
                productsToUpdate.add(Product(it.first, catPair.first,"", it.second.first, ""))
            }
        }
        productRepo.addStockToProduct(productsToUpdate) //todo: this should update the category collection?
    }

    fun onProductButtonPress(categoryName:String, productName:String, add: Boolean):String{
        val categoryMap:HashMap<String, HashMap<String, Pair<Int, Int>>> = inventoryUIState.categoryUIMap
        var newAddNum: Int = if (add){
            categoryMap[categoryName]!![productName]!!.second + 1
        }else{
            categoryMap[categoryName]!![productName]!!.second - 1
        }
        var newMap:HashMap<String, HashMap<String, Pair<Int, Int>>> = categoryMap.clone() as HashMap<String, HashMap<String, Pair<Int, Int>>>
        newMap[categoryName]!![productName] = Pair(categoryMap[categoryName]!![productName]!!.first, newAddNum)
        inventoryUIState  = inventoryUIState.copy(categoryUIMap = newMap)
        return newAddNum.toString()
    }

    fun onAddNumChange(categoryName: String, productName: String, input: String):String{
        val maxInt = BigInteger(Int.MAX_VALUE.toString())
        val categoryMap:HashMap<String, HashMap<String, Pair<Int, Int>>> = inventoryUIState.categoryUIMap
        if (input != "" && input != "-" && BigInteger(input) < maxInt) {
            var newMap:HashMap<String, HashMap<String, Pair<Int, Int>>> = categoryMap.clone() as HashMap<String, HashMap<String, Pair<Int, Int>>>
            newMap[categoryName]!![productName] = Pair(categoryMap[categoryName]!![productName]!!.first, input.toInt())
            inventoryUIState  = inventoryUIState.copy(categoryUIMap = newMap)
            return input
        }
        return ""
    }
}
