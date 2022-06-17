package com.example.ofn.inventory

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import java.math.BigInteger
import com.example.ofn.ui.theme.OFNButtonColors


// todo: save to local storage?
// todo: check if values are valid (e.g. no negative available amounts, values don't go out of bounds and crash)
// todo: make it look good on horizontal view
@Composable
fun InventoryScreen(navController: NavController) {
    var exampleCategories = listOf(
        Category(
            name = "Cat 1",
            produceList = listOf(Produce("1","produce 0",1), Produce("12","produce 0",0), Produce("13","produce 0",0))
        ),
        Category(
            name = "Cat 2",
            produceList = listOf(Produce("2","produce 0",0), Produce("3","produce 0",0), Produce("4","produce 0",0))
        ),
        Category(
            name = "Cat 3",
            produceList = listOf(Produce("5","produce 0",0), Produce("6","produce 0",0), Produce("7","produce 0",0), Produce("19","produce 0",0))
        ),
        Category(
            name = "Cat 4",
            produceList = listOf(Produce("14","produce 0",0), Produce("15","produce 0",0), Produce("16","produce 0",0))
        ),
        Category(
            name = "Cat 5",
            produceList = listOf(Produce("17","produce 0",0), Produce("18","produce 0",0), Produce("19","produce 0",0))
        )
    )

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
                    .padding(16.dp)
            ) {
                // todo: function to search inventory
            }
            // sort + filter
            Row(
                modifier = Modifier
                    .padding(vertical = 5.dp),
            ) {
                SortDropdown()
                Spacer(modifier = Modifier.size(24.dp))
                FilterDropdown(exampleCategories)
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }

    Box(modifier = modifier) {
        TextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            placeholder = { Text("Search for item...") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.background,
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colors.onPrimary,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(MaterialTheme.colors.background, CircleShape)
                .padding(horizontal = 20.dp, vertical = 5.dp),
            maxLines = 1,
            singleLine = true,
            leadingIcon = { Icon(
                Icons.Filled.Search,
                contentDescription = "",
                modifier = Modifier
            ) },
        )
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
                    Icons.Filled.KeyboardArrowUp
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

                // -------------------- Produce --------------------
                if (expanded) {
                    categoryItem.produceList.forEach { produce ->
                        item(key = produce.id) {
                            ProduceItem(produce)
                        }
                    }
                }
            }
            // -------------------- Reset / Submit Buttons --------------------
            item {
                Row {
                    // Reset button
                    Button(
                        colors = OFNButtonColors(),
                        modifier = Modifier
                            .padding(30.dp),
                        onClick = {
                            reset(categories)
                            categories.forEachIndexed { i, categoryItem ->
                                expandedState[i] = false
                            }
                        },
                    ) {
                        Text(
                            text = "Reset",
                            fontSize = 20.sp,
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .size(80.dp)
                    )
                    // Save button
                    Button(
                        colors = OFNButtonColors(),
                        modifier = Modifier
                            .padding(30.dp),
                        onClick = {
                            refresh.value = false
                            save(categories)
                            reset(categories)
                            refresh.value = true
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
fun ProduceItem(produce: Produce) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 25.dp, bottom = 25.dp, start = 15.dp)
            .fillMaxSize()
    ) {
        // Produce Name
        Text(
            text = produce.name,
            modifier = Modifier.padding(end = 30.dp)
        )

        // Produce amount available
        Text(
            text = "${produce.amount} available",
            fontSize = 10.sp,
            modifier = Modifier
                .padding(end = 45.dp)
                .width(50.dp)
        )

        ProduceButtons(produce)
    }
    Divider()
}

@Composable
fun ProduceButtons(produce: Produce) {
    val maxInt = BigInteger(Int.MAX_VALUE.toString())
    var addNumStr by remember(produce.amount) {
        mutableStateOf(produce.addNum.toString())
    }
    val interactionSourceAdd = remember { MutableInteractionSource() }
    val interactionSourceRemove = remember { MutableInteractionSource() }

    if (interactionSourceRemove.collectIsPressedAsState().value) {
        if (produce.amount > Int.MIN_VALUE) {
            produce.addNum--
            addNumStr = produce.addNum.toString()
        }
    }
    if (interactionSourceAdd.collectIsPressedAsState().value) {
        if (produce.addNum < Int.MAX_VALUE) {
            produce.addNum++
            addNumStr = produce.addNum.toString()
        }
    }

    // Button to remove produce
    Button(
        colors = OFNButtonColors(),
        modifier = Modifier
            .size(45.dp),
        onClick = {
            if (produce.addNum > Int.MIN_VALUE) {
                produce.addNum--
                addNumStr = produce.addNum.toString()
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

    // Text field to specify number of produce to add/remove
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
                    produce.addNum = it.toInt()
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

    // Button to add produce
    Button(
        colors = OFNButtonColors(),
        modifier = Modifier
            .size(45.dp),
        onClick = {
            if (produce.addNum < Int.MAX_VALUE) {
                produce.addNum++
                addNumStr = produce.addNum.toString()
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
        category.produceList.forEach{ produce: Produce ->
            produce.addNum = 0
        }
    }
}

fun save(sections: List<Category>) {
    sections.forEach{ category: Category ->
        category.produceList.forEach{ produce: Produce ->
            produce.amount += produce.addNum
        }
    }
}

@Composable
fun SortDropdown() {
    val options = listOf("Name", "Price", "Amount")
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(0) }
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Box (
        modifier = Modifier
            .clickable(
                onClick = {
                    expanded = !expanded
                }
            )
            .width(180.dp)
    ) {
        TextField(
            value = options[selectedOption],
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = { Text("Sort By") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.background,
                disabledTextColor = MaterialTheme.colors.onSecondary,
                disabledLabelColor = MaterialTheme.colors.onSecondary,
                disabledTrailingIconColor = MaterialTheme.colors.onSecondary,
                cursorColor = Color.Transparent,
            ),
            trailingIcon = {
                Icon(
                    icon,
                    contentDescription = "",
                    modifier = Modifier
                )
            },
        )
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {
            expanded = false
        },
        modifier = Modifier.width(180.dp)
    ) {
        options.forEachIndexed() { i, option ->
            DropdownMenuItem(
                onClick = {
                    selectedOption = i
                    expanded = false
                    //todo: sort results
                }
            ) {
                Text(
                    text = option
                )
            }
        }
    }
}

@Composable
fun FilterDropdown(options: List<Category>) {
    val optionCheckedState = remember { options.map { true }.toMutableStateList() }
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Box(
        modifier = Modifier
            .clickable(
                onClick = {
                    expanded = !expanded
                }
            )
            .width(180.dp)
    ) {
        TextField(
            value = "Filter By",
            onValueChange = {},
            readOnly = true,
            enabled = false,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.background,
                disabledTextColor = MaterialTheme.colors.onSecondary,
                disabledTrailingIconColor = MaterialTheme.colors.onSecondary,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = Color.Transparent,
            ),
            trailingIcon = {
                Icon(
                    icon,
                    contentDescription = "",
                    modifier = Modifier
                )
            },
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier
                .width(180.dp)
        ) {
            options.forEachIndexed() { i, option ->
                DropdownMenuItem(
                    onClick = {
                        optionCheckedState[i] = !optionCheckedState[i]
                    },
                ) {
                    Row {
                        Checkbox(
                            checked = optionCheckedState[i],
                            onCheckedChange = {
                                optionCheckedState[i] = !optionCheckedState[i]
                                // todo: filter results
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colors.onPrimary,
                                uncheckedColor = MaterialTheme.colors.onSecondary,
                                checkmarkColor = MaterialTheme.colors.onSecondary,
                            )
                        )
                        Text(
                            text = option.name
                        )
                    }
                }
            }
        }
    }
}

data class Category(val name: String, val produceList: List<Produce>)
data class Produce(val id: String, val name: String, var amount: Int, var addNum: Int = 0)