package com.example.ofn.settings

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
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
import com.example.ofn.presentation.components.FormTextField
import com.example.ofn.presentation.components.bottomsheet.BottomSheetContent
import com.example.ofn.presentation.settings.account.AccountFormViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AccountScreen(navController: NavController?, accountFormViewModel: AccountFormViewModel) {
    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val name:String by accountFormViewModel.name.observeAsState("")
    val email:String by accountFormViewModel.email.observeAsState("")
    val phone:String by accountFormViewModel.phone.observeAsState("")
    val imageUri:Uri? by accountFormViewModel.imageUri.observeAsState(null)
    val bitmap:Bitmap? by accountFormViewModel.bitmap.observeAsState(null)
    val isCameraSelected:Boolean by accountFormViewModel.isCameraSelected.observeAsState(false)

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        accountFormViewModel.onImageUriChange(uri)
        accountFormViewModel.onBitmapChange(null)
    }
    val cameraLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) { btm: Bitmap? ->
        accountFormViewModel.onImageUriChange(null)
        accountFormViewModel.onBitmapChange(btm)
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
                            accountFormViewModel.onCameraSelected(true)
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
                            accountFormViewModel.onCameraSelected(false)
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
//        scrimColor = Color.Green
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(30.dp)
            ) {
                Text(
                    "Edit Account",
                    fontSize = 30.sp,
                    modifier = Modifier.padding(0.dp)
                )

                val focusManager = LocalFocusManager.current
                Column(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

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
                                .size(180.dp)
                                .border(
                                    width = 2.dp,
                                    color = Color.Blue,
                                    shape = CircleShape
                                )
                                .padding(4.dp)
                                .clip(CircleShape)
                        )
                    }
                    else if (bitmap == null){
                        accountFormViewModel.onBitmapChange(
                            (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                val source = ImageDecoder.createSource(context.contentResolver, imageUri!!)
                                ImageDecoder.decodeBitmap(source)
                            } else {
                                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri!! )
                            })
                        )
                        Image(
                            bitmap = bitmap!!.asImageBitmap(),
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
                    else{
                        Image(
                            bitmap = bitmap!!.asImageBitmap(),
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
                    Button(
                        onClick = {
                            scope.launch {
                                if (!modalBottomSheetState.isVisible){
                                    modalBottomSheetState.show()
                                } else{
                                    modalBottomSheetState.hide()
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(5.dp)
                            .height(35.dp)
                    ) {
                        Text(text = "Update Profile Picture")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, bottom = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.width(70.dp)
                        ) {
                            Text("Name")
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(Icons.Outlined.Person, contentDescription = "Name Icon")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        FormTextField(
                            text = name,
                            placeholder = "Name",
                            onValueChange = { accountFormViewModel.onNameChange(it) },
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
                        Row(
                            modifier = Modifier.width(70.dp)
                        ){
                            Text("Phone")
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(Icons.Outlined.Phone, contentDescription = "Phone Icon")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        FormTextField(
                            text = phone,
                            placeholder = "Phone",
                            onValueChange = { accountFormViewModel.onPhoneChange(it) },
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
                        Row(
                            modifier = Modifier.width(70.dp)
                        ) {
                            Text("Email")
                            Spacer(modifier = Modifier.width(3.dp))
                            Icon(Icons.Outlined.Email, contentDescription = "Email Icon")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        FormTextField(
                            text = email,
                            placeholder = "Email",
                            onValueChange = { accountFormViewModel.onEmailChange(it) },
                            imeAction = ImeAction.Next,//Show next as IME button
                            keyboardType = KeyboardType.Text, //Plain text keyboard
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            ),
                            visualTransformation = VisualTransformation.None
                        )
                    }

                }
            }
        }
    }
}
