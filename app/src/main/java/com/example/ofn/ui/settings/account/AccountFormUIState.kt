package com.example.ofn.ui.settings.account

import android.graphics.Bitmap
import android.net.Uri

data class AccountFormUIState(
    val userName: String = "",
    val email: String = "",
    val phone: String = "",
    val imageUri: Uri? = null,
    val bitmap: Bitmap? = null,
    val isCameraSelected: Boolean = false
)