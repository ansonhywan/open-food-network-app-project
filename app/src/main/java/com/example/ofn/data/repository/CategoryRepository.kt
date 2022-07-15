package com.example.ofn.data.repository
import android.util.Log
import com.example.ofn.data.Constants
import com.example.ofn.data.dao.CategoryDao
import com.example.ofn.data.model.Category
import com.example.ofn.data.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class CategoryRepository(){

    private val categoriesDao = CategoryDao()

    fun addNewCategoryAndProduct(productName: String, categoryName: String, description: String) {
        categoriesDao.postNewCategoryAndProduct(productName, categoryName, description)
    }

    suspend fun getAllCategories(onComplete: (HashMap<Boolean, Any>) -> Unit)=withContext(Dispatchers.IO){
        val allCategoriesList = mutableListOf<Category>()
        categoriesDao.getAllCategories().addOnCompleteListener {
            if (it.isSuccessful){
                for (document in it.result){
                    val categoryName = document.get("categoryName") as String
                    allCategoriesList.add(Category(categoryName = categoryName))
                }

            }
        }
    }

    fun renameCategory(categoryName: String, newName: String) {
        categoriesDao.renameCategory(categoryName, newName)
    }

    fun deleteCategory(categoryName: String){
        categoriesDao.deleteCategory(categoryName)
    }

    suspend fun testGetCategories(onComplete:(HashMap<Boolean, Any>)->Unit)= withContext(Dispatchers.IO) {
        categoriesDao.testGetCategories()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    onComplete.invoke(hashMapOf<Boolean, Any>(true to it.result.documents))
                }else{
                    onComplete.invoke(hashMapOf<Boolean, Any>(false to Constants.RETRIEVE_DATA_FAILURE_MESSAGE))
                }
            }
    }

}