package com.example.ofn.ui.settings.manage

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.material.Icon
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.ofn.ui.navigation.Screen
import com.example.ofn.ui.components.FormTextField
import com.example.ofn.ui.components.bottomsheet.BottomSheetContent
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ManageScreen(navController: NavController?, manageViewModel: ManageViewModel) {
    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val manageUIState:ManageUIState = manageViewModel.manageUIState
    Log.d("ManageScreen", "help " + manageUIState.toString())
    var name:String = manageUIState.productName
    var category:String = manageUIState.category
    val description:String = manageUIState.description
    val imageUri:Uri? = manageUIState.imageUri
    val bitmap:Bitmap? = manageUIState.bitmap
    val isCameraSelected:Boolean = manageUIState.isCameraSelected
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        manageViewModel.onImageUriChange(uri)
        manageViewModel.onBitmapChange(null)
    }
    val cameraLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) { btm: Bitmap? ->
        manageViewModel.onImageUriChange(null)
        manageViewModel.onBitmapChange(btm)
    }
    val permissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
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
                            manageViewModel.onCameraSelected(true)
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
                            manageViewModel.onCameraSelected(false)
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
                    .padding(horizontal = 20.dp)
                    .padding(top = 30.dp)
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        "Edit Product",
                        fontSize = 30.sp,
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
                                .size(150.dp)
                                .border(
                                    width = 2.dp,
                                    color = Color.Blue,
                                    shape = CircleShape
                                )
                                .padding(4.dp)
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
                    else if (bitmap == null){
                        manageViewModel.onBitmapChange(
                            (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                val source = ImageDecoder.createSource(context.contentResolver, imageUri!!)
                                ImageDecoder.decodeBitmap(source)
                            } else {
                                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri!! )
                            })
                        )
//                        Image(
//                            bitmap = bitmap!!.asImageBitmap(),
//                            contentDescription = "Profile Image",
//                            alignment = Alignment.TopCenter,
//                            modifier = Modifier
//                                .wrapContentWidth()
//                                .size(150.dp)
//                                .border(
//                                    width = 2.dp,
//                                    color = Color.Blue,
//                                    shape = CircleShape
//                                )
//                                .padding(4.dp)
//                                .clip(CircleShape)
//                                .clickable{
//                                    scope.launch {
//                                        if (!modalBottomSheetState.isVisible){
//                                            modalBottomSheetState.show()
//                                        } else{
//                                            modalBottomSheetState.hide()
//                                        }
//
//                                    }
//                                },
//                            contentScale = ContentScale.Crop
//                        )
                    }
                    else{
                        Image(
                            bitmap = bitmap!!.asImageBitmap(),
                            contentDescription = "Profile Image",
                            alignment = Alignment.TopCenter,
                            modifier = Modifier
                                .wrapContentWidth()
                                .size(150.dp)
                                .border(
                                    width = 2.dp,
                                    color = Color.Blue,
                                    shape = CircleShape
                                )
                                .padding(4.dp)
                                .clip(CircleShape)
                                .clickable{
                                    scope.launch {
                                        if (!modalBottomSheetState.isVisible){
                                            modalBottomSheetState.show()
                                        } else{
                                            modalBottomSheetState.hide()
                                        }

                                    }
                                },
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
                        Box(modifier = Modifier.width(90.dp)) {
                            Text("Name")
                        }
                        FormTextField(
                            text = name,
                            placeholder = "Name",
                            onValueChange = { manageViewModel.onNameChange(it) },
                            imeAction = ImeAction.Next,//Show next as IME button
                            keyboardType = KeyboardType.Text, //Plain text keyboard
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                            ),
                            visualTransformation = VisualTransformation.None
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.width(90.dp)) {
                            Text("Category")
                        }
                        FormTextField(
                            text = category,
                            placeholder = "Category",
                            onValueChange = { manageViewModel.onCategoryChange(it) },
                            imeAction = ImeAction.Next,//Show next as IME button
                            keyboardType = KeyboardType.Text, //Plain text keyboard
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                            ),
                            visualTransformation = VisualTransformation.None
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
                        Box(modifier = Modifier.width(90.dp)) {
                            Text("Description")
                        }
                        FormTextField(
                            modifier = Modifier.height(200.dp),
                            text = description,
                            placeholder = "Description",
                            onValueChange = { manageViewModel.onDescriptionChange(it) },
                            imeAction = ImeAction.Next,//Show next as IME button
                            keyboardType = KeyboardType.Text, //Plain text keyboard
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                            ),
                            visualTransformation = VisualTransformation.None
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .wrapContentHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Button(
                            onClick = {
                                manageViewModel.onProductDelete(context, category)
//                                    if (retval) {
//                                        Toast.makeText(
//                                            context,
//                                            "Product Deleted!",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                        manageViewModel.resetToDefault()
//                                    } else {
//                                        Toast.makeText(
//                                            context,
//                                            "Product was not Deleted. Try Again",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    }
//                                    navController?.navigate(Screen.ManageProductsAndCategories.route)
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
                                val retval = manageViewModel.onProductSaved(name, category, description);
                                if (retval) {
                                    manageViewModel.resetToDefault()
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
                                manageViewModel.resetToDefault()
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
