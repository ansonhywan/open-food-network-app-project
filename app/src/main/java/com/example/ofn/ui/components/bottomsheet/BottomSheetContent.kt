package com.example.ofn.ui.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ofn.R

@Composable
fun BottomSheetContent(onCameraClick: () -> Unit, onGalleryClick: ()->Unit, onCancelClick: ()->Unit){
    val context = LocalContext.current
    Column {
        BottomSheetListItem(
            icon = R.drawable.camera,
            title = "Camera",
            onItemClick = {
                onCameraClick()
            })
        Divider(
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 1.dp),
            thickness = 1.dp
        )
        BottomSheetListItem(
            icon = R.drawable.gallery,
            title = "Gallery",
            onItemClick = {
                onGalleryClick()
            }
        )
        Divider(
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 1.dp),
            thickness = 1.dp
        )
        BottomSheetListItem(
            icon = R.drawable.cancel,
            title = "Cancel",
            onItemClick = {
                onCancelClick()
            }
        )
        Divider(
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 1.dp),
            thickness = 1.dp
        )
    }
}

@Composable
fun BottomSheetListItem(icon: Int, title: String, onItemClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(onClick = { onItemClick(title) })
            .height(55.dp)
            .background(color = colorResource(id = R.color.purple_500))
            .padding(start = 15.dp)
            .padding(vertical = 15.dp)
        , verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = painterResource(id = icon), contentDescription = "BottomSheet Item Icon", tint = Color.White)
        Spacer(modifier = Modifier.width(30.dp))
        Text(text = title, color = Color.White, fontSize = 20.sp)
    }
}
