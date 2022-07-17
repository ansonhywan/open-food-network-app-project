package com.example.ofn.ui.inventory

import android.content.Context
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

    val inventoryUIState: InventoryUIState = inventoryViewModel.inventoryUIState

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        ExpandableCategories(
            inventoryViewModel = inventoryViewModel,
            categories = inventoryUIState.categoryUIMap,
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
                FilterDropdown(inventoryUIState.categoryUIMap.keys.toList())
            }
        }
    }
}

@Composable
fun ExpandableCategories(
    inventoryViewModel: InventoryViewModel,
    categories: HashMap<String, HashMap<String, Pair<Int, Int>>>,
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit
) {
    val categoryNames: List<String> = categories.keys.toList()
    val expandedState = remember(categoryNames) { categoryNames.map { false }.toMutableStateList() }
    val context = LocalContext.current

    Column(modifier) {
        header()
        LazyColumn(

        ) {
            // -------------------- Categories --------------------
            categoryNames.forEachIndexed { i, categoryName ->
                val expanded = expandedState[i]
                val icon = if (expanded)
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
                            text = categoryName,
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
                    item {
                        CategoryItem(categoryName, inventoryViewModel)
                    }
                }
            }
        }
            // -------------------- Reset / Submit Buttons --------------------

        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Reset button
            Button(
                colors = OFNButtonColors(),
                onClick = {
                    inventoryViewModel.resetAllAddNum()
                    categoryNames.forEachIndexed { i, _ ->
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

@Composable
fun CategoryItem(categoryName: String, inventoryViewModel: InventoryViewModel) {
    inventoryViewModel.inventoryUIState.categoryUIMap[categoryName]!!.forEach { (productName, productInfo) ->
        ProductItem(categoryName, productName, inventoryViewModel)
    }
}

@Composable
fun ProductItem(categoryName: String, productName: String, inventoryViewModel: InventoryViewModel) {
    val productInfo: Pair<Int, Int> = inventoryViewModel.inventoryUIState.categoryUIMap[categoryName]?.get(productName)!!
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 25.dp, bottom = 25.dp, start = 15.dp)
            .fillMaxSize()
    ) {
        // Product Name
        Text(
            text = productName,
            modifier = Modifier
                .padding(end = 30.dp)
                .width(80.dp)
        )

        // Product amount available
        Text(
            text = "${productInfo.first} available",
            fontSize = 10.sp,
            modifier = Modifier
                .padding(end = 30.dp)
                .width(50.dp)
        )

        ProductButtons(categoryName, productName,  inventoryViewModel)
    }
    Divider()
}

@Composable
fun ProductButtons(categoryName: String, productName: String,inventoryViewModel: InventoryViewModel) {
    var addNum:String by remember  { mutableStateOf(inventoryViewModel.inventoryUIState.categoryUIMap[categoryName]!![productName]!!.second.toString()) }
    val interactionSourceAdd = remember { MutableInteractionSource() }
    val interactionSourceRemove = remember { MutableInteractionSource() }

    if (interactionSourceRemove.collectIsPressedAsState().value) {
        addNum = inventoryViewModel.onProductButtonPress(categoryName, productName, false)
    }
    if (interactionSourceAdd.collectIsPressedAsState().value) {
        addNum = inventoryViewModel.onProductButtonPress(categoryName, productName, true)
    }

    // Button to remove product
    Button(
        colors = OFNButtonColors(),
        modifier = Modifier
            .size(45.dp),
        onClick = {
            addNum = inventoryViewModel.onProductButtonPress(categoryName, productName, false)
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
            value = addNum,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {
                addNum = inventoryViewModel.onAddNumChange(categoryName, productName, it)
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
            addNum = inventoryViewModel.onProductButtonPress(categoryName, productName, true)
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

