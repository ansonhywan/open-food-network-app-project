package com.example.ofn.ui.inventory

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
import com.example.ofn.data.model.Category
import com.example.ofn.data.model.Product
import com.example.ofn.ui.components.FilterDropdown
import com.example.ofn.ui.components.SearchBar
import com.example.ofn.ui.components.SortDropdown
import com.example.ofn.ui.theme.OFNButtonColors


// todo: save to local storage?
// todo: check if values are valid (e.g. no negative available amounts, values don't go out of bounds and crash)
// todo: make it look good on horizontal view

@Composable
fun InventoryScreen(navController: NavController, inventoryViewModel: InventoryViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        ExpandableCategories(
            inventoryViewModel = inventoryViewModel,
            categories = inventoryViewModel.categories.sortedWith(compareBy { it.categoryName }),
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
                FilterDropdown(inventoryViewModel.categories.sortedWith(compareBy { it.categoryName }).map{ category -> category.categoryName })
            }
        }
    }
}

@Composable
fun ExpandableCategories(
    inventoryViewModel: InventoryViewModel,
    categories: List<Category>,
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit
) {
    val expandedState = remember(categories) { categories.map { false }.toMutableStateList() }
    val context = LocalContext.current

    if(inventoryViewModel.refreshState.value) {
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
                            text = categoryItem.categoryName,
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
                    item{
                        CategoryItem(categoryItem.categoryName, inventoryViewModel)
                    }
                }
            }
            // -------------------- Reset / Submit Buttons --------------------
            item {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 100.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // Reset button
                    Button(
                        colors = OFNButtonColors(),
                        onClick = {
                            inventoryViewModel.onReset()
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
                            inventoryViewModel.onSave()
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
fun CategoryItem(name: String, inventoryViewModel: InventoryViewModel) {
    val productList = remember { inventoryViewModel.getProductList(name) }
    productList.forEach { product ->
        ProductItem(product, inventoryViewModel)
    }
}

@Composable
fun ProductItem(product: Product, inventoryViewModel: InventoryViewModel) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 25.dp, bottom = 25.dp, start = 15.dp)
            .fillMaxSize()
    ) {
        // Product Name
        Text(
            text = product.productName,
            modifier = Modifier
                .padding(end = 30.dp)
                .width(80.dp)
        )

        // Product amount available
        Text(
            text = "${product.stock} available",
            fontSize = 10.sp,
            modifier = Modifier
                .padding(end = 30.dp)
                .width(50.dp)
        )

        ProductButtons(product, inventoryViewModel)
    }
    Divider()
}

@Composable
fun ProductButtons(product: Product, inventoryViewModel: InventoryViewModel) {
    var addNumStr: String by remember(inventoryViewModel.refreshState) {
        mutableStateOf(inventoryViewModel.addNumMap[product.category]?.get(product.productName)?.addNum.toString())
    }
    if(inventoryViewModel.addNumMap[product.category]?.get(product.productName)?.addNum == null) {
        addNumStr = "0"
    }
    val interactionSourceAdd = remember { MutableInteractionSource() }
    val interactionSourceRemove = remember { MutableInteractionSource() }

    if (interactionSourceRemove.collectIsPressedAsState().value) {
        addNumStr = inventoryViewModel.onProductButtonPress(product, false)
    }
    if (interactionSourceAdd.collectIsPressedAsState().value) {
        addNumStr = inventoryViewModel.onProductButtonPress(product, true)
    }

    // Button to remove product
    Button(
        colors = OFNButtonColors(),
        modifier = Modifier
            .size(45.dp),
        onClick = {
            addNumStr = inventoryViewModel.onProductButtonPress(product, false)
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
                addNumStr = inventoryViewModel.onAddNumChange(product, it, addNumStr)
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
            addNumStr = inventoryViewModel.onProductButtonPress(product, true)
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

