package com.example.ofn.ui.settings.manage

import android.graphics.Bitmap
import android.net.Uri
data class ManageUIState(
    val productName: String = "",
    val category: String = "",
    val description: String = "",
    val imageUri: Uri? = null,
    val bitmap: Bitmap? = null,
    val isCameraSelected: Boolean = false
)