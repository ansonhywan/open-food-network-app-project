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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.ofn.R
import com.example.ofn.Screen
import com.example.ofn.components.FormTextField
import com.example.ofn.components.bottomsheet.BottomSheetContent
import com.example.ofn.settings.manage.ManageViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Composable
fun ManageScreen(navController: NavController?, viewModel:ManageViewModel = ManageViewModel()) {
    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    var isCameraSelected by rememberSaveable{ mutableStateOf<Boolean>(false) }
    var imageUri by rememberSaveable{ mutableStateOf<Uri?>(null) }
    var bitmap by rememberSaveable{ mutableStateOf<Bitmap?>(null) }

    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
            bitmap = null
    }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { btm: Bitmap? ->
        bitmap = btm
        imageUri = null
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            if (isCameraSelected) {
                cameraLauncher.launch()
            } else {
                galleryLauncher.launch("image/*")
            }
            scope.launch {
                modalBottomSheetState.hide()
            }
        } else {
            Toast.makeText(context, "Permission Denied!", Toast.LENGTH_SHORT).show()
        }
    }
    ModalBottomSheetLayout(
        sheetContent = {
            BottomSheetContent(
                onCameraClick = {
                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(
                            context, Manifest.permission.CAMERA
                        ) -> {
                            cameraLauncher.launch()
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                        }
                        else -> {
                            isCameraSelected = true
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                }, onGalleryClick = {
                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(
                            context, Manifest.permission.READ_EXTERNAL_STORAGE
                        ) -> {
                            galleryLauncher.launch("image/*")
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                        }
                        else -> {
                            isCameraSelected = false
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    }
                }, onCancelClick = {
                    scope.launch { modalBottomSheetState.hide() }
                })
        },
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetBackgroundColor = Color.Red,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(30.dp)
            ) {
                Row (
                    modifier = Modifier
                        .padding(2.dp) ,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        "Edit Product",
                        fontSize = 30.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                    val placeHolderImage =
                        "https://tedblob.com/wp-content/uploads/2021/09/android.png"
                    if (imageUri == null && bitmap == null){
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(context)
                                    .crossfade(false)
                                    .data(placeHolderImage)
                                    .build(),
                                filterQuality = FilterQuality.High
                            ),
                            contentDescription = "Profile Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .wrapContentWidth()
                                .size(100.dp)
                                .border(
                                    width = 2.dp,
                                    color = Color.Blue,
                                    shape = CircleShape
                                )
                                .padding(10.dp)
                                .clip(CircleShape)
                                .clickable{
                                    scope.launch {
                                        if (!modalBottomSheetState.isVisible){
                                            modalBottomSheetState.show()
                                        } else{
                                            modalBottomSheetState.hide()
                                        }

                                    }
                                }
                        )
                    }
                    imageUri?.let {
                        if (!isCameraSelected) {
                            val source = ImageDecoder.createSource(context.contentResolver, it)
                            bitmap = ImageDecoder.decodeBitmap(source)
                        }
                    }

                    bitmap?.let { btm ->
                        Image(
                            bitmap = btm.asImageBitmap(),
                            contentDescription = "Profile Image",
                            alignment = Alignment.TopCenter,
                            modifier = Modifier
                                .wrapContentWidth()
                                .size(200.dp)
                                .border(
                                    width = 2.dp,
                                    color = Color.Blue,
                                    shape = CircleShape
                                )
                                .padding(4.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                val focusManager = LocalFocusManager.current
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, bottom = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Name")
                        FormTextField(
                            text = viewModel.name,
                            placeholder = "Name",
                            onChange = { viewModel.onNameChange(it) },
                            imeAction = ImeAction.Next,//Show next as IME button
                            keyboardType = KeyboardType.Text, //Plain text keyboard
                            keyBoardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                            )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Category")
                        FormTextField(
                            text = viewModel.category,
                            placeholder = "Category",
                            onChange = { viewModel.onCategoryChange(it) },
                            imeAction = ImeAction.Next,//Show next as IME button
                            keyboardType = KeyboardType.Text, //Plain text keyboard
                            keyBoardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                            )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Description")
                        FormTextField(
                            modifier = Modifier.height(200.dp),
                            text = viewModel.description,
                            placeholder = "Description",
                            onChange = { viewModel.onDescriptionChange(it) },
                            imeAction = ImeAction.Next,//Show next as IME button
                            keyboardType = KeyboardType.Text, //Plain text keyboard
                            keyBoardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                            )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .wrapContentHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Button(
                                onClick = {
                                    val retval = viewModel.onProductDelete();
                                    if (retval) {
                                        Toast.makeText(
                                            context,
                                            "Product Deleted!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Product was not Deleted. Try Again",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    navController?.navigate(Screen.ManageProductsAndCategories.route)
                                    //return
                                },
                                modifier = Modifier
                                    .padding(5.dp)
                                    .height(35.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.DeleteOutline,
                                    contentDescription = "Delete Icon"
                                )
                                Text(text = "Delete")
                            }
                        }
                        Column(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .wrapContentHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Button(
                                onClick = {
                                    val retval = viewModel.onProductSaved();
                                    if (retval) {
                                        Toast.makeText(context, "Product Saved!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Product was not Saved.", Toast.LENGTH_SHORT).show()
                                    }
                                    navController?.navigate(Screen.ManageProductsAndCategories.route)
                                },
                                modifier = Modifier
                                    .padding(5.dp)
                                    .height(35.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Save,
                                    contentDescription = "Save Icon"
                                )
                                Text(text = "Save")
                            }
                        }
                        Column(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .wrapContentHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Button(
                                onClick = {
                                    navController?.navigate(Screen.ManageProductsAndCategories.route)
                                },
                                modifier = Modifier
                                    .padding(5.dp)
                                    .height(35.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Cancel,
                                    contentDescription = "Return Icon"
                                )
                                Text(text = "Cancel")
                            }
                        }
                    }

                }
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalPermissionsApi::class)
@Preview(showBackground = true)
@Composable
fun ManagePreview() {
    ManageScreen(navController = null)
}