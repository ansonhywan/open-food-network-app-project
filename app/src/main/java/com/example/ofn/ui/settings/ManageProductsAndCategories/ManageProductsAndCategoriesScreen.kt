package com.example.ofn.ui.settings.ManageProductsAndCategories

import android.util.Log
import android.widget.MediaController
import androidx.compose.material.Icon
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ofn.data.model.Category
import com.example.ofn.data.model.Product
import com.example.ofn.ui.navigation.Screen
import com.example.ofn.ui.components.SearchBar
import com.example.ofn.ui.inventory.InventoryUIState
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.navArgument

@Composable
fun ManageProductsAndCategoriesScreen(navController: NavController, viewModel: ManageProductsAndCategoriesViewModel) {
    val manageProductsAndCategoriesUIState: ManageProductsAndCategoriesUIState = viewModel.manageProductsAndCategoriesUIState

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Categories(
            navController = navController,
            viewModel = viewModel,
            categories = manageProductsAndCategoriesUIState.categoryUIMap,
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Manage",
                fontSize = 35.sp,
                fontWeight = FontWeight.Medium
            )
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholderText = "Search ..."
            ) {
            }
        }
    }
}

@Composable
fun Categories(
    navController: NavController,
    viewModel: ManageProductsAndCategoriesViewModel,
    categories: HashMap<String, HashMap<String, Pair<Int, Int>>>,
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit
) {
    val categoryNames: MutableList<String> = categories.keys.toMutableStateList()
    val expandedState = remember(categories) { categories.map { false }.toMutableStateList() }
    val dropdownState = remember(categories) { categories.map { false }.toMutableStateList() }
    val openRenameDialog = remember { mutableStateOf(false)  }
    val openDeleteDialog = remember { mutableStateOf(false)  }
    var change by remember { mutableStateOf("") }
    val context = LocalContext.current

    LazyColumn(modifier) {
        item {
            header()
        }
        categoryNames.forEachIndexed { i, categoryItem ->
            val expanded = expandedState[i]
            val dropdown = dropdownState[i]
            val categoryName = categoryNames[i]
            val categoryMenuIcon = Icons.Filled.Menu
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
                    Icon(
                        icon,
                        contentDescription = "dropdown arrow",
                        tint = MaterialTheme.colors.onSecondary,
                        modifier = Modifier
                    )
                    Spacer(
                        modifier = Modifier
                            .size(1.dp)
                    )
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
                    Box(modifier = Modifier) {
                        IconButton(
                            onClick = { dropdownState[i] = !dropdown }
                        ) {
                            Icon(
                                categoryMenuIcon,
                                contentDescription = "category menu icon",
                                tint = MaterialTheme.colors.onSecondary,
                                modifier = Modifier
                            )
                        }
                        DropdownMenu(
                            expanded = dropdownState[i],
                            onDismissRequest = { dropdownState[i] = false }
                        )
                        {
                                DropdownMenuItem(onClick = { openRenameDialog.value = true }) {
                                    Text(text = "Rename")
                                    val name = categoryName;
                                    Log.d("rename", categoryName)
                                    if (openRenameDialog.value) {
                                        var text by remember { mutableStateOf(name) }
                                        AlertDialog(
                                            onDismissRequest = {
                                                openRenameDialog.value = false;
                                                change = text;
                                            },
                                            title = {
                                                Text(text = "Rename")
                                            },
                                            text = {
                                                Column() {
                                                    TextField(
                                                        value = text,
                                                        onValueChange = { text = it }
                                                    )
                                                }
                                            },
                                            buttons = {
                                                Row(
                                                    modifier = Modifier.padding(all = 8.dp),
                                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Button(
                                                        onClick = {
                                                            viewModel.renameCategory(
                                                                name,
                                                                text
                                                            );
                                                            if (true) {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Rename Successfull",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            } else {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Rename Unsucccessful",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                            openRenameDialog.value = false;
                                                        }
                                                    ) {
                                                        Text("Confirm")
                                                    }
                                                    Spacer(
                                                        modifier = Modifier
                                                            .size(24.dp)
                                                    )
                                                    Button(
                                                        onClick = {
                                                            openRenameDialog.value = false;
                                                            Toast.makeText(
                                                                context,
                                                                "Category was not renamed",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    ) {
                                                        Text("Dismiss")
                                                    }
                                                }
                                            }
                                        )
                                    };
                                }
                                DropdownMenuItem(onClick = { openDeleteDialog.value = true }) {
                                        Text(text = "Delete")
                                        val name = categoryName;
                                        if (openDeleteDialog.value) {
                                            AlertDialog(
                                                onDismissRequest = {
                                                    openDeleteDialog.value = false;
                                                },
                                                title = {
                                                    Text(text = "Are you sure you would like to delete the Category: " + name)
                                                },
                                                buttons = {
                                                    Row(
                                                        modifier = Modifier.padding(all = 8.dp),
                                                        horizontalArrangement = Arrangement.SpaceEvenly,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Button(onClick = {
                                                            openDeleteDialog.value = false;
                                                            var retval = true;
                                                            viewModel.deleteCategory(name);
                                                            if (retval) {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Delete Successfull",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            } else {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Delete Unsucccessful",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                        }
                                                        ) {
                                                            Text("Confirm")
                                                        }
                                                        Spacer(
                                                            modifier = Modifier
                                                                .size(24.dp)
                                                        )
                                                        Button(
                                                            onClick = {
                                                                openDeleteDialog.value = false
                                                                Toast.makeText(
                                                                    context,
                                                                    "Category was not renamed",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                        ) {
                                                            Text("Cancel")
                                                        }
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                    }
                }

                Divider()
            }

            if (expanded) {
                item {
                    CategoryProducts(navController, categoryName, categories)
                }
            }
        }
        item {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp)
            ) {
                addNewProductButton(navController)
            }
        }
    }
}


@Composable
fun addNewProductButton(navController: NavController) {
    // Add new category button
    val onClick = { addNewProduct(navController ) }
    FloatingActionButton(onClick = onClick) {
        Icon(Icons.Filled.Add,"")
    }
}

@Composable
fun CategoryProducts(navController: NavController, categoryName: String, categories:HashMap<String, HashMap<String, Pair<Int, Int>>>) {
    val allProducts: HashMap<String, Pair<Int, Int>> = categories[categoryName]!!
    allProducts.keys.forEach {
        val pname = it;
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(top = 5.dp, bottom = 5.dp, start = 5.dp)
                .fillMaxSize()
        ) {
            Text(
                text = it
            )
            IconButton(
                onClick = {
                    goToProductManageScreen(navController, pname, categoryName);
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = "Got to product page"
                )
            }
        }
    }

    Divider()
}


fun addNewProduct(navController: NavController) {
    navController.navigate(Screen.ManageProduct.route)
}

fun goToProductManageScreen(navController: NavController, productName: String, category: String) {
    navController.navigate("manage_screen/?productName=$productName?category=$category")
}