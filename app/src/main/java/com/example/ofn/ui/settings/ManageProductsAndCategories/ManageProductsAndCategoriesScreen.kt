package com.example.ofn.ui.settings.ManageProductsAndCategories

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

@Composable
fun ManageProductsAndCategoriesScreen(navController: NavController, viewModel: ManageProductsAndCategoriesViewModel) {
    val manageProductsAndCategoriesUIState: ManageProductsAndCategoriesUIState = viewModel.manageProductsAndCategoriesUIState

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Categories(
            navController = navController,
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
    categories: HashMap<String, HashMap<String, Pair<Int, Int>>>,
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit
) {
    val categoryNames: List<String> = categories.keys.toList()
    val expandedState = remember(categories) { categories.map { false }.toMutableStateList() }

    LazyColumn(modifier) {
        item {
            header()
        }
        categoryNames.forEachIndexed { i, categoryName ->
            val expanded = expandedState[i]
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
                    Icon(
                        categoryMenuIcon,
                        contentDescription = "category menu icon",
                        tint = MaterialTheme.colors.onSecondary,
                        modifier = Modifier
                        //todo: do pop up menu to edit category
                    )


                }
                Divider()
            }

            if (expanded) {
                item {
                    CategoryProducts(categoryName, categories)
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
fun CategoryProducts(categoryName: String, categories:HashMap<String, HashMap<String, Pair<Int, Int>>>) {
    val allProducts: HashMap<String, Pair<Int, Int>> = categories[categoryName]!!
    allProducts.keys.forEach {
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
                    //go to new page
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