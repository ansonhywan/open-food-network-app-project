package com.example.ofn.settings

import android.os.Build
import androidx.compose.material.Icon
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ofn.navigation.Screen
import com.example.ofn.components.SearchBar
import com.example.ofn.inventory.Category
import com.example.ofn.inventory.Product
import com.example.ofn.settings.ManageProductsAndCategories.ManageProductsAndCategoriesViewModel
import com.example.ofn.settings.manage.ManageViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi


@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Composable
fun ManageProductsAndCategoriesScreen(navController: NavController?, viewModel: ManageProductsAndCategoriesViewModel= androidx.lifecycle.viewmodel.compose.viewModel()) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        if (navController != null) {
            Categories(
                navController = navController,
                categories = viewModel.getCategories(),
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

}

@Composable
fun Categories(
    navController: NavController,
    categories: List<Category>,
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit
) {
    val expandedState = remember(categories) { categories.map { false }.toMutableStateList() }
    val refresh = remember { mutableStateOf(true) }

    if(refresh.value) {
        LazyColumn(modifier) {
            item {
                header()
            }
            categories.forEachIndexed { i, categoryItem ->
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
                    categoryItem.productList.forEach { product ->
                        item(key = product.id) {
                            CategoryProducts(product)
                        }
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
fun CategoryProducts(product: Product) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(top = 5.dp, bottom = 5.dp, start = 5.dp)
            .fillMaxSize()
    ) {
        Text(
            text = product.name
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
    Divider()
}

fun addNewProduct(navController: NavController) {
    navController?.navigate(Screen.ManageProduct.route)

}

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalPermissionsApi::class)
@Preview(showBackground = true)
@Composable
fun ManageProductsAndCategoriesPreview() {
    ManageProductsAndCategoriesScreen(navController = null)
}