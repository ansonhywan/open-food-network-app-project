package com.example.ofn.settings

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.material.Icon
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.ofn.R
import com.example.ofn.Screen
import com.example.ofn.components.FilterDropdown
import com.example.ofn.components.FormTextField
import com.example.ofn.components.SearchBar
import com.example.ofn.components.SortDropdown
import com.example.ofn.components.bottomsheet.BottomSheetContent
import com.example.ofn.inventory.Category
import com.example.ofn.inventory.ExpandableCategories
import com.example.ofn.inventory.Produce
import com.example.ofn.settings.ManageProductsAndCategories.ManageProductsAndCategoriesViewModel
import com.example.ofn.ui.theme.OFNButtonColors
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch
import java.math.BigInteger


@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Composable
fun ManageProductsAndCategoriesScreen(navController: NavController?, viewModel:ManageProductsAndCategoriesViewModel = ManageProductsAndCategoriesViewModel()) {
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
    val dropdownState = remember(categories) { categories.map { false }.toMutableStateList() }
    val refresh = remember { mutableStateOf(true) }

    if(refresh.value) {
        LazyColumn(modifier) {
            item {
                header()
            }
            categories.forEachIndexed { i, categoryItem ->
                val expanded = expandedState[i]
                val dropdown = dropdownState[i]
                val categoryMenuIcon = Icons.Filled.Menu
                val icon = if(expanded)
                    Icons.Filled.KeyboardArrowUp
                else
                    Icons.Filled.KeyboardArrowDown
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
                            ) {
                                DropdownMenuItem(onClick = { /*TODO*/ }) {
                                    Text(text = "Rename")
                                };
                                DropdownMenuItem(onClick = { /*TODO*/ }) {
                                    Text(text = "Delete")
                                }
                            }
                        }
                    }
                    Divider()
                }

                if (expanded) {
                    categoryItem.produceList.forEach { produce ->
                        item(key = produce.id) {
                            CategoryProducts(produce)
                        }
                    }
                }
            }
            item {
                Row {
                    Spacer(
                        modifier = Modifier
                            .size(300.dp)
                    )
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
fun CategoryProducts(produce: Produce) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 5.dp, bottom = 5.dp, start = 5.dp)
            .fillMaxSize()
    ) {
        Text(
            text = produce.name,
            modifier = Modifier.padding(end = 30.dp)
        )
        Spacer(
            modifier = Modifier
                .size(24.dp)
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