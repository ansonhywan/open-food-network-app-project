package com.example.ofn.ui.platform

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.ofn.ui.components.SortType

data class PlatformUIState(
    val filterList: SnapshotStateList<String> = mutableStateListOf<String>(),
    val sort: MutableState<SortType> = mutableStateOf(SortType.ASC),
    val searchStr: MutableState<String> = mutableStateOf(""),
    )
