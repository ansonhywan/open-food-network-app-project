package com.example.ofn.settings

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
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
import com.example.ofn.components.FormTextField
import com.example.ofn.settings.account.AccountFormViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

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
            Text("Edit Account", fontSize = 30.sp)
            val focusManager = LocalFocusManager.current
            Column(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .wrapContentHeight()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp),
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    var imageUri = remember { mutableStateOf<Uri?>(null) }
                    val context = LocalContext.current
                    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){
                            uri -> imageUri.value = uri
                    }
                    val placeHolderImage = "https://image.shutterstock.com/image-vector/ui-image-placeholder-wireframes-apps-260nw-1037719204.jpg"

                    Text("Picture")
                    Icon(Icons.Outlined.Face, contentDescription = "Picture Icon")
                    Button(
                        onClick = { galleryLauncher.launch("image/*") },
                        modifier = Modifier.width(250.dp)
                    ) {
                        Text("Choose File")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)

@Preview(showBackground = true)
@Composable
fun AccountPreview() {
    AccountScreen(navController = null)
}