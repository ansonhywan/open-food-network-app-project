package com.example.ofn.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SortDropdown(options: List<String>) {
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
fun FilterDropdown(options: List<String>) {
    val optionCheckedState = remember { options.map { true }.toMutableStateList() }
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowRight
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
                            text = option
                        )
                    }
                }
            }
        }
    }
}
