package com.example.ofn.platform

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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.ofn.components.*
import java.math.BigInteger
import com.example.ofn.ui.theme.OFNButtonColors


// todo: save to local storage?
// todo: check if values are valid (e.g. no negative available amounts, values don't go out of bounds and crash)
// todo: make it look good on horizontal view
@Composable
fun PlatformScreen(navController: NavController) {
    var examplePlatforms = listOf(
        Platform(
            name = "Platform 1",
            produceList = listOf(PlatformProduce("1","produce 0",1.5, 1), PlatformProduce("12","produce 0",1.5,0), PlatformProduce("13","produce 0",0.5))
        ),
        Platform(
            name = "Platform 2",
            produceList = listOf(PlatformProduce("2","produce 0",1.5,0), PlatformProduce("3","produce 0",1.5,0), PlatformProduce("4","produce 0",0.5))
        ),
        Platform(
            name = "Platform 3",
            produceList = listOf(PlatformProduce("5","produce 0",1.5,0), PlatformProduce("6","produce 0",1.5,0), PlatformProduce("7","produce 0",0.5), PlatformProduce("19","produce 0",0.5))
        ),
        Platform(
            name = "Platform 4",
            produceList = listOf(PlatformProduce("14","produce 0",1.5,0), PlatformProduce("15","produce 0",1.5,0), PlatformProduce("16","produce 0",0.5))
        ),
        Platform(
            name = "Platform 5",
            produceList = listOf(PlatformProduce("17","produce 0",1.5,0), PlatformProduce("18","produce 0",1.5,0), PlatformProduce("19","produce 0",0.5))
        )
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        ExpandablePlatforms(
            platforms = examplePlatforms,
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
                // todo: function to search platform
            }
            // sort + filter
            Row(
                modifier = Modifier
                    .padding(vertical = 5.dp),
            ) {
                SortDropdown(listOf("Name", "Price", "Amount"))
                Spacer(modifier = Modifier.size(24.dp))
                FilterDropdown(examplePlatforms.map{platform -> platform.name })
            }
        }
    }
}

@Composable
fun ExpandablePlatforms(
    platforms: List<Platform>,
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit
) {
    val expandedState = remember(platforms) { platforms.map { false }.toMutableStateList() }
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
            platforms.forEachIndexed { i, platformItem ->
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
                            approve(platforms)
                            refresh.value = true
                            Toast.makeText(context, "Approved!", Toast.LENGTH_SHORT).show()
                        },
                    ) {
                        Text(
                            text = "Approve",
                            fontSize = 20.sp,
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
                modifier = Modifier.padding(end = 30.dp)
            )
            Spacer(modifier = Modifier.size(20.dp))
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
            modifier = Modifier.padding(end = 30.dp)
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
