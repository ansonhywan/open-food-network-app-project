package com.example.ofn.ui.settings.ManageProductsAndCategories

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ofn.data.model.Category
import com.example.ofn.data.model.Product
import com.example.ofn.data.repository.CategoryRepository
import com.example.ofn.ui.inventory.InventoryUIState
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class ManageProductsAndCategoriesViewModel(private val categoryRepository: CategoryRepository = CategoryRepository()) : ViewModel() {


    var manageProductsAndCategoriesUIState by mutableStateOf(ManageProductsAndCategoriesUIState())
    fun populateCategories()=viewModelScope.launch{
        categoryRepository.getAllCategoriesProducts {
            if (it.containsKey(true)){
                val categoryInfo:Pair<String, Pair<String, Int>>  = it[true] as Pair<String, Pair<String, Int>>
                val categoryName: String = categoryInfo.first
                val productName: String = categoryInfo.second.first
                val stock: Int = categoryInfo.second.second
                Log.d("Populate", manageProductsAndCategoriesUIState.categoryUIMap.toString())
                val newMap: HashMap<String, HashMap<String, Pair<Int, Int>>> = manageProductsAndCategoriesUIState.categoryUIMap.clone() as HashMap<String, HashMap<String, Pair<Int, Int>>>
                if (manageProductsAndCategoriesUIState.categoryUIMap.containsKey(categoryName)) {
                    newMap[categoryName]!!.put(productName, Pair(stock, 0))
                }
                else{
                    val newProductEntry: HashMap<String, Pair<Int, Int>> =  hashMapOf(productName to Pair(stock, 0))
                    newMap[categoryName] = newProductEntry
                }
                manageProductsAndCategoriesUIState  = manageProductsAndCategoriesUIState.copy(categoryUIMap = newMap)
            }else{
                Log.d("Populate", "Failed to populate all categories")
            }
        }
    }


    fun renameCategory(categoryName:String, newCategoryName:String) {
        runBlocking {
            launch {
                categoryRepository.renameCategory(categoryName, newCategoryName)
                    .addOnCompleteListener({
                        Log.d("rename1 ", "aye");
                        populateCategories();
                        Log.d("rename2 ", "aye");
                        //call function to renam category and get return value from it to determine if it was correct
                    })
            }
        }
    }

    fun deleteCategory(categoryName: String):Boolean {
        var retval = true;
        categoryRepository.deleteCategory(categoryName);
        populateCategories();
        //call function to delete category and get return value form it to determine if it was success
        return retval;
    }

}