package com.example.ofn.ui.platform

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ofn.ui.components.FilterDropdown
import com.example.ofn.ui.components.SearchBar
import com.example.ofn.ui.components.SortDropdown
import com.example.ofn.ui.components.SortType
import com.example.ofn.ui.inventory.InventoryUIState
import com.example.ofn.ui.theme.OFNButtonColors


// todo: save to local storage?
// todo: check if values are valid (e.g. no negative available amounts, values don't go out of bounds and crash)
// todo: make it look good on horizontal view
@Composable
fun PlatformScreen(navController: NavController, platformViewModel: PlatformViewModel) {
    val platformUIState: PlatformUIState = platformViewModel.platformUIState

    var examplePlatforms = listOf(
        Platform(
            name = "P1aa1latform 1",
            produceList = listOf(PlatformProduce("1","produce 0",1.5, 1), PlatformProduce("12","produce 0",1.5,0), PlatformProduce("13","produce 0",0.5))
        ),
        Platform(
            name = "aaPlatform 2",
            produceList = listOf(PlatformProduce("2","produce 0",1.5,0), PlatformProduce("3","produce 0",1.5,0), PlatformProduce("4","produce 0",0.5))
        ),
        Platform(
            name = "bbPlatform 3",
            produceList = listOf(PlatformProduce("5","produce 0",1.5,0), PlatformProduce("6","produce 0",1.5,0), PlatformProduce("7","produce 0",0.5), PlatformProduce("19","produce 0",0.5))
        ),
        Platform(
            name = "vvvPlatform 4",
            produceList = listOf(PlatformProduce("14","produce 0",1.5,0), PlatformProduce("15","produce 0",1.5,0), PlatformProduce("16","produce 0",0.5))
        ),
        Platform(
            name = "ccPlatform 5",
            produceList = listOf(PlatformProduce("17","produce 0",1.5,0), PlatformProduce("18","produce 0",1.5,0), PlatformProduce("19","produce 0",0.5))
        )
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        ExpandablePlatforms(
            platformViewModel = platformViewModel,
            platforms = examplePlatforms.filter { !platformUIState.filterList.contains(it.name) && it.name.contains(platformUIState.searchStr.value) },
            modifier = Modifier
                .padding(16.dp)
        ) {
            // title
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Platform",
                fontSize = 35.sp,
                fontWeight = FontWeight.Medium
            )
            // search
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholderText = "Search platform..."
            ) {
                platformUIState.searchStr.value = it
            }
            // sort + filter
            Row(
                modifier = Modifier
                    .padding(vertical = 5.dp),
            ) {
                SortDropdown(listOf("A->Z", "Z->A")) {
                    option -> run {
                        if (option.equals("A->Z")) {
                            platformUIState.sort.value = SortType.ASC
                        } else {
                            platformUIState.sort.value = SortType.DESC
                        }
                    }
                }
                Spacer(modifier = Modifier.size(24.dp))
                FilterDropdown(examplePlatforms.map{platform -> platform.name }) { platName, isChecked ->
                    run {
                        if(isChecked) {
                            platformUIState.filterList.remove(platName)
                        } else {
                            platformUIState.filterList.add(platName)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpandablePlatforms(
    platformViewModel: PlatformViewModel,
    platforms: List<Platform>,
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit
) {
    val sortedPlatforms = if (platformViewModel.platformUIState.sort.value == SortType.ASC)
        platforms.sortedBy { it.name }
    else platforms.sortedByDescending { it.name }

    val expandedState = remember(sortedPlatforms) { sortedPlatforms.map { false }.toMutableStateList() }
    val refresh = remember { mutableStateOf(true) }
    val context = LocalContext.current

    if(refresh.value) {
        LazyColumn(
            modifier
        ) {
            item {
                header()
            }
            // -------------------- Platforms --------------------
            sortedPlatforms.forEachIndexed { i, platformItem ->
                val expanded = expandedState[i]
                val icon = if(expanded)
                    Icons.Filled.KeyboardArrowDown
                else
                    Icons.Filled.KeyboardArrowRight
                item(key = "platform $i") {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                expandedState[i] = !expanded
                            }
                    ) {
                        Text(
                            text = platformItem.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
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

                // -------------------- Produce --------------------
                if (expanded) {
                    platformItem.produceList.forEach { produce ->
                        item(key = produce.id) {
                            ProduceItemPlatform(produce)
                        }
                    }
                }
            }
            // -------------------- Approve Buttons --------------------
            item {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    // Save button
                    Button(
                        colors = OFNButtonColors(),
                        modifier = Modifier
                            .padding(30.dp),
                        onClick = {
                            refresh.value = false
                            approve(sortedPlatforms)
                            refresh.value = true
                            Toast.makeText(context, "Approved!", Toast.LENGTH_SHORT).show()
                        },
                    ) {
                        Text(
                            text = "Approve",
                            fontSize = 25.sp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProduceItemPlatform(produce: PlatformProduce) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 25.dp, bottom = 25.dp, start = 15.dp)
            .fillMaxSize()
    ) {
        Column {
            // Produce Name
            Text(
                text = produce.name,
                fontSize = 25.sp,
                modifier = Modifier.padding(end = 30.dp)
            )
            Spacer(modifier = Modifier.size(50.dp))
            ProduceTextFieldsPlatform(produce)
        }
    }
    Divider()
}

@Composable
fun ProduceTextFieldsPlatform(produce: PlatformProduce) {
    var priceStr by remember(produce.price) {
        mutableStateOf(produce.price.toString())
    }
    var amountStr by remember(produce.amount) {
        mutableStateOf(produce.amount.toString())
    }

    // Price text field
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
    ) {
        Text(
            text = "Price",
            modifier = Modifier.padding(end = 20.dp),
            fontSize = 15.sp,
        )
        Column(
            modifier = Modifier
                .width(80.dp)
        ) {
            TextField(
                value = priceStr,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = {
                    // todo: do more verification on the correctness of the input
                    priceStr = it
                    produce.price = priceStr.toDouble()
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
        Spacer(modifier = Modifier.size(30.dp))
        Text(
            text = "Amount",
            modifier = Modifier.padding(end = 30.dp),
            fontSize = 15.sp,
            )
        Column(
            modifier = Modifier
                .width(80.dp)
        ) {
            TextField(
                value = amountStr,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = {
                    // todo: do more verification on the correctness of the input
                    amountStr = it
                    produce.amount = amountStr.toInt()
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
    }
}

fun approve(sections: List<Platform>) {
    sections.forEach{ platform: Platform ->
        platform.produceList.forEach{ produce: PlatformProduce ->
            // todo: send to database
        }
    }
}

fun getName(platform: Platform): String {
    return platform.name
}

data class Platform(val name: String, val produceList: List<PlatformProduce>)
data class PlatformProduce(val id: String, val name: String, var price: Double = 0.0, var amount: Int = 0)
