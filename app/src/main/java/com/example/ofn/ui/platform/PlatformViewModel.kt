package com.example.ofn.ui.platform

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ofn.data.model.Product
import com.example.ofn.data.repository.CategoryRepository
import com.example.ofn.data.repository.ProductRepository
import kotlinx.coroutines.launch
import java.math.BigInteger

class PlatformViewModel(
    private val categoryRepo: CategoryRepository = CategoryRepository(),
    private val productRepo: ProductRepository = ProductRepository()
): ViewModel() {

    var platformUIState by mutableStateOf(PlatformUIState())
}
