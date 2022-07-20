package com.example.ofn.data.repository
import android.util.Log
import com.example.ofn.data.Constants
import com.example.ofn.data.dao.CategoryDao
import com.example.ofn.data.dao.ProductDao
import com.example.ofn.data.model.Category
import com.example.ofn.data.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class CategoryRepository(){

    private val categoriesDao = CategoryDao()
    private val productsDao = ProductDao()

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

    fun addNewCategoryAndProduct(productName: String, categoryName: String, description: String): Boolean {
        val newProduct = Product(
            productName = productName,
            category = categoryName,
            description = description,
            stock = 0,
            imageUrl = "temp"
        )

        val newCategory = Category(
            categoryName = categoryName,
            productList = listOf()
        )

       categoriesDao.getCategoryWithName(categoryName)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.isEmpty) {
                        // Category does not exist.
                        Log.d("postNewCategoryAndProd", "CATEGORY DOES NOT EXIST")
                        categoriesDao.addNewCategory(newCategory)
                            .addOnSuccessListener { docRef ->
                                docRef
                                    .collection("products")
                                    .add(newProduct)
                            }
                    } else {
                        // Category already exists.
                        Log.d("postNewCategoryAndProd", "CATEGORY ALREADY EXISTS")
                        val docId = task.result.documents[0].id
                        val categoryProductsRef = categoriesDao.getCategoryWithId(docId).collection("products")
                        productsDao.getProductWithName(categoryProductsRef, productName)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    if (task.result.isEmpty) {
                                        // Product does not already exist. Add it to sub-collection
                                        categoryProductsRef
                                            .add(newProduct)
                                    } else {
                                        // Product already exists.
                                        // Do nothing.
                                        Log.d("postNewCategoryAndProd", "$productName ALREADY EXISTS in $categoryName")
                                    }
                                }
                            }
                    }
                }
            }

        // TODO: ERROR CHECKING, CURRENTLY ALWAYS RETURNS TRUE.
        return true;
    }

    fun renameCategory(categoryName: String, newName: String) {
        categoriesDao.getCategoryWithName(categoryName)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // Query successful, category to rename exists.
                    val categoryRef = categoriesDao.getCategoryWithId(it.result.documents[0].id)
                    categoryRef
                        .update("categoryName", newName)
                        .addOnSuccessListener { Log.d("renameCategory", "$categoryName successfully renamed to $newName") }
                        .addOnFailureListener { Log.d("renameCategory", "Error renaming $categoryName") }
                } else {
                    Log.d("deleteCategory", "Query to find category unsuccessful.")
                }
            }
    }

    fun deleteCategory(categoryName: String){
        categoriesDao.getCategoryWithName(categoryName)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // Query successful, category to delete exists.
                    val categoryRef = categoriesDao.getCategoryWithId(it.result.documents[0].id)
                    categoriesDao.getProductsUnderCategory(it.result.documents[0].id)
                        .addOnSuccessListener { result ->
                            for (doc in result) {
                                val product = categoryRef.collection("products").document(doc.id)
                                product
                                    .delete()
                                    .addOnSuccessListener { Log.d("deleteCategory", "Successfully deleted product: ${product.id}") }
                                    .addOnFailureListener { e -> Log.w("deleteCategory", "Error deleting product", e) }
                            }
                        }
                    // Finished deleting products under the category. Now delete the category.
                    categoryRef
                        .delete()
                        .addOnSuccessListener { Log.d("deleteCategory", "Successfully deleted Category: ${categoryRef.id}") }
                        .addOnFailureListener { e -> Log.w("deleteCategory", "Error deleting document", e) }
                } else {
                    Log.d("deleteCategory", "Query to find category unsuccessful.")
                }
            }
    }


}