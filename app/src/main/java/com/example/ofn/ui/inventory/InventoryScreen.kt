package com.example.ofn.ui.inventory

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.ofn.data.repository.CategoryRepository
import com.example.ofn.data.repository.InventoryRepository
import com.example.ofn.ui.components.FilterDropdown
import com.example.ofn.ui.components.SearchBar
import com.example.ofn.ui.components.SortDropdown
import java.math.BigInteger
import com.example.ofn.ui.theme.OFNButtonColors
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


// todo: save to local storage?
// todo: check if values are valid (e.g. no negative available amounts, values don't go out of bounds and crash)
// todo: make it look good on horizontal view

private val firestoreDB = Firebase.firestore //TODO: REMOVE THIS WHEN REPOSITORIES ARE IMPLEMENTED

private val inventoryRepo = InventoryRepository()
private val categoryRepo = CategoryRepository()

fun updateInventory(productList: List<Product>) {
    // Should update since if there is a product in the Inventory Page, it is already in the DB.
    productList.forEach{
        Log.i("updateInventory", "${it.name}, ${it.amount}")
        firestoreDB.collection("inventory").document(it.name)
            .update("stock", it.amount)
            //.addOnSuccessListener { Log.d(TAG, "${it.name} stock successfully updated!") }
            //.addOnFailureListener { e -> Log.w(TAG, "Error updating ${it.name}", e) }
    }
}


@Composable
fun InventoryScreen(navController: NavController) {
    var exampleCategories = listOf(
        Category(
            name = "Fruits",
            productList = listOf(Product("1","Bananas",1), Product("2","Cherries",0), Product("3","Blueberries",0))
        ),
        Category(
            name = "Vegetables",
            productList = listOf(Product("4","Asparagus",0), Product("5","Avocado",0), Product("6","Broccoli",0))
        ),
        Category(
            name = "Dairy",
            productList = listOf(Product("7","Butter",0), Product("8","Cheese",0), Product("9","Milk",0))
        ),
        Category(
            name = "Meat",
            productList = listOf(Product("10","Bacon",0), Product("11","Beef",0), Product("12","Chicken",0))
        )
    ).sortedWith(compareBy { it.name })

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        ExpandableCategories(
            categories = exampleCategories,
            modifier = Modifier
                .padding(16.dp)
        ) {
            // title
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Inventory",
                fontSize = 35.sp,
                fontWeight = FontWeight.Medium
            )
            // search
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholderText = "Search product..."
            ) {
                // todo: function to search inventory
            }
            // sort + filter
            Row(
                modifier = Modifier
                    .padding(vertical = 5.dp),
            ) {
                SortDropdown(listOf("Name", "Price", "Amount"))
                Spacer(modifier = Modifier.size(24.dp))
                FilterDropdown(exampleCategories.map{ category -> category.name })
            }
        }
    }
}

@Composable
fun ExpandableCategories(
    categories: List<Category>,
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit
) {
    val expandedState = remember(categories) { categories.map { false }.toMutableStateList() }
    val refresh = remember { mutableStateOf(true) }
    val context = LocalContext.current

    if(refresh.value) {
        LazyColumn(modifier) {
            item {
                header()
            }
            // -------------------- Categories --------------------
            categories.forEachIndexed { i, categoryItem ->
                val expanded = expandedState[i]
                val icon = if(expanded)
                    Icons.Filled.KeyboardArrowDown
                else
                    Icons.Filled.KeyboardArrowRight
                item(key = "category $i") {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                expandedState[i] = !expanded
                            }
                    ) {
                        Text(
                            text = categoryItem.name,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(vertical = 25.dp)
                                .weight(11f)
                        )
                        Spacer(
                            modifier = Modifier
                                .size(24.dp)
                        )
                        Icon(
                            icon,
                            contentDescription = "dropdown arrow",
                            tint = MaterialTheme.colors.onSecondary,
                            modifier = Modifier
                        )
                    }
                    Divider()
                }

                // -------------------- Product --------------------
                if (expanded) {
                    categoryItem.productList.forEach { product ->
                        item(key = product.id) {
                            ProductItem(product)
                        }
                    }
                }
            }
            // -------------------- Reset / Submit Buttons --------------------
            item {
                Row(
                    modifier = Modifier.fillMaxSize().padding(top=100.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // Reset button
                    Button(
                        colors = OFNButtonColors(),
                        onClick = {
                            reset(categories)
                            categories.forEachIndexed { i, categoryItem ->
                                expandedState[i] = false
                            }
                            Toast.makeText(context, "Cleared all inputs!", Toast.LENGTH_SHORT).show()
                        },
                    ) {
                        Text(
                            text = "Reset",
                            fontSize = 20.sp,
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .size(24.dp)
                    )
                    // Save button
                    Button(
                        colors = OFNButtonColors(),
                        modifier = Modifier
                            .wrapContentSize(),
                        onClick = {
                            refresh.value = false
                            save(categories)
                            reset(categories)
                            refresh.value = true

                            // Get number in the field and update backend database.
                            categories.forEach { categoryItem ->
                                updateInventory(categoryItem.productList)
                            }

                            Toast.makeText(context, "Inventory Saved!", Toast.LENGTH_SHORT).show()
                        },
                    ) {
                        Text(
                            text = "Save",
                            fontSize = 20.sp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 25.dp, bottom = 25.dp, start = 15.dp)
            .fillMaxSize()
    ) {
        // Product Name
        Text(
            text = product.name,
            modifier = Modifier
                .padding(end = 30.dp)
                .width(80.dp)
        )

        // Product amount available
        Text(
            text = "${product.amount} available",
            fontSize = 10.sp,
            modifier = Modifier
                .padding(end = 30.dp)
                .width(50.dp)
        )

        ProductButtons(product)
    }
    Divider()
}

@Composable
fun ProductButtons(product: Product) {
    val maxInt = BigInteger(Int.MAX_VALUE.toString())
    var addNumStr by remember(product.amount) {
        mutableStateOf(product.addNum.toString())
    }
    val interactionSourceAdd = remember { MutableInteractionSource() }
    val interactionSourceRemove = remember { MutableInteractionSource() }

    if (interactionSourceRemove.collectIsPressedAsState().value) {
        if (product.amount > Int.MIN_VALUE) {
            product.addNum--
            addNumStr = product.addNum.toString()
        }
    }
    if (interactionSourceAdd.collectIsPressedAsState().value) {
        if (product.addNum < Int.MAX_VALUE) {
            product.addNum++
            addNumStr = product.addNum.toString()
        }
    }

    // Button to remove product
    Button(
        colors = OFNButtonColors(),
        modifier = Modifier
            .size(45.dp),
        onClick = {
            if (product.addNum > Int.MIN_VALUE) {
                product.addNum--
                addNumStr = product.addNum.toString()
            }
        },
        contentPadding = PaddingValues(
            start = 1.dp,
            top = 1.dp,
            end = 1.dp,
            bottom = 1.dp,
        ),
        shape = CircleShape,
        interactionSource = interactionSourceRemove
    ) {
        Icon(
            imageVector = Icons.Filled.Remove,
            contentDescription = "Remove inventory",
            modifier = Modifier
                .size(30.dp),
        )
    }

    // Text field to specify number of product to add/remove
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier
            .width(80.dp)
            .padding(5.dp),
    ) {
        TextField(
            value = addNumStr,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {
                // todo: do more verification on the correctness of the input
                if (it != "" && it != "-" && BigInteger(it) < maxInt) {
                    product.addNum = it.toInt()
                }
                addNumStr = it
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.background,
                focusedIndicatorColor = MaterialTheme.colors.onPrimary,
                cursorColor = MaterialTheme.colors.onSecondary,
            ),
            maxLines = 1,
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
        )
    }

    // Button to add product
    Button(
        colors = OFNButtonColors(),
        modifier = Modifier
            .size(45.dp),
        onClick = {
            if (product.addNum < Int.MAX_VALUE) {
                product.addNum++
                addNumStr = product.addNum.toString()
            }
        },
        contentPadding = PaddingValues(
            start = 1.dp,
            top = 1.dp,
            end = 1.dp,
            bottom = 1.dp,
        ),
        interactionSource = interactionSourceAdd
    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = "Add inventory",
            modifier = Modifier
                .size(35.dp)
        )
    }
}

fun reset(sections: List<Category>) {
    sections.forEach{ category: Category ->
        category.productList.forEach{ product: Product ->
            product.addNum = 0
        }
    }
}

fun save(sections: List<Category>) {
    sections.forEach{ category: Category ->
        category.productList.forEach{ product: Product ->
            product.amount += product.addNum
        }
    }
}

data class Category(val name: String, val productList: List<Product>)
data class Product(val id: String, val name: String, var amount: Int, var addNum: Int = 0)
