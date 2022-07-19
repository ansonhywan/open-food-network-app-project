package com.example.ofn.data.repository
import android.util.Log
import com.example.ofn.data.Constants
import com.example.ofn.data.dao.CategoryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class CategoryRepository(){

    private val categoriesDao = CategoryDao()

    suspend fun getAllCategories(onComplete:(HashMap<Boolean, Any>)->Unit)= withContext(Dispatchers.IO) {
        categoriesDao.getAllCategories()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onComplete.invoke(hashMapOf<Boolean, Any>(true to it.result.documents))
                } else {
                    onComplete.invoke(hashMapOf<Boolean, Any>(false to Constants.RETRIEVE_DATA_FAILURE_MESSAGE))
                }
            }
    }

    suspend fun getAllCategoriesProducts(onComplete: (HashMap<Boolean, Pair<String, Pair<String, Int>>?>) -> Unit)= withContext(Dispatchers.Default){
        categoriesDao.getAllCategories()
            .addOnCompleteListener {
                Log.d("Get All", "finished getting everything")
                if (it.isSuccessful){
                    for (category in it.result){
                        val categoryName:String = category.get("categoryName") as String
                        val categoryId: String = category.id
                        categoriesDao.getProductsUnderCategory(categoryId).addOnCompleteListener { task->
                            if (task.isSuccessful){
                                for (product in task.result){
                                    val productName: String = product.get("productName").toString()
                                    val stock: Int = product.get("stock").toString().toInt()
                                    onComplete.invoke(hashMapOf(true to Pair(categoryName, Pair(productName, stock))))
                                }
                            }else{
                                onComplete.invoke(hashMapOf(false to null))
                            }
                        }
                    }
                }else{
                    Log.d("Failed", "Failed to get all categories")
                    onComplete.invoke(hashMapOf(false to null))
                }
            }
    }
    fun addNewCategoryAndProduct(productName: String, categoryName: String, description: String) {
        categoriesDao.postNewCategoryAndProduct(productName, categoryName, description)
    }


    fun renameCategory(categoryName: String, newName: String) {
        categoriesDao.renameCategory(categoryName, newName)
    }

    fun deleteCategory(categoryName: String){
        categoriesDao.deleteCategory(categoryName)
    }


}