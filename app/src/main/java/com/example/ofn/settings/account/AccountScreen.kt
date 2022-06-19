package com.example.ofn.settings

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.ofn.R
import com.example.ofn.components.FormTextField
import com.example.ofn.settings.account.AccountFormViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi


@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AccountScreen(navController: NavController?, viewModel:AccountFormViewModel = AccountFormViewModel()) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.LightGray,
    ){
        Column(
            modifier = Modifier
                .padding(30.dp)
        ){
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
                var imageUri = remember { mutableStateOf<Uri?>(null) }
                val context = LocalContext.current
                val galleryLanucher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){
                    uri: Uri? -> imageUri.value = uri
                }
                val placeHolderImage = "https://tedblob.com/wp-content/uploads/2021/09/android.png"
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(context)
                            .crossfade(false)
                            .data(imageUri.value?:placeHolderImage)
                            .build(),
                        filterQuality = FilterQuality.High
                    ),
                    contentDescription = "Profile Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .wrapContentWidth()
                        .size(150.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .border(BorderStroke(2.dp, Color.Blue), CircleShape)
                )
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.padding(5.dp).height(35.dp)
                ) {
                    Text(text = "Update Profile Picture")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text("Name")
                    Icon(Icons.Outlined.Person, contentDescription = "Name Icon")
                    FormTextField(
                        text = viewModel.name,
                        placeholder = "Name",
                        onChange = {viewModel.onNameChange(it)},
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
                ){
                    Text("Phone")
                    Icon(Icons.Outlined.Phone, contentDescription = "Phone Icon")
                    FormTextField(
                        text = viewModel.phone,
                        placeholder = "Phone",
                        onChange = {viewModel.onPhoneChange(it)},
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
                ){
                    Text("Email")
                    Icon(Icons.Outlined.Email, contentDescription = "Email Icon")
                    FormTextField(
                        text = viewModel.email,
                        placeholder = "Email",
                        onChange = {viewModel.onEmailChange(it)},
                        imeAction = ImeAction.Next,//Show next as IME button
                        keyboardType = KeyboardType.Text, //Plain text keyboard
                        keyBoardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        )
                    )
                }

            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalPermissionsApi::class)

@Preview(showBackground = true)
@Composable
fun AccountPreview() {
    AccountScreen(navController = null)
}