package com.example.ofn.ui.settings

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.ofn.ui.navigation.Screen
import com.example.ofn.ui.components.NavigationPanel
import com.example.ofn.ui.navigation.AUTH_GRAPH_ROUTE
import com.example.ofn.ui.settings.account.AccountFormUIState
import com.example.ofn.ui.settings.account.AccountFormViewModel


@Composable
fun SettingsScreen(navController: NavController, accountFormViewModel: AccountFormViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            ProfileCard(accountFormViewModel)
            Column(
                modifier = Modifier
                    .padding(7.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NavigationPanel(
                    text = "Edit Account"
                ) {
                    navController.navigate(Screen.Account.route)
                }
                NavigationPanel(
                    text = "Manage Product & Categories"
                ) {
                    navController.navigate(Screen.ManageProductsAndCategories.route)
                }
            }
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().padding(bottom = 10.dp)
            ) {
                Button(
                    colors = ButtonDefaults.textButtonColors(backgroundColor = Color.Red, contentColor = Color.White),
                    onClick = { navController.navigate(AUTH_GRAPH_ROUTE) }) {
                    Text("Sign Out",  fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                }
            }
        }
    }
}


@Composable
fun ProfileCard(accountFormViewModel: AccountFormViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .padding(bottom = 16.dp),
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Card(
                shape = CircleShape,
                border = BorderStroke(width = 2.dp, color = Color.Green),
                modifier = Modifier.padding(16.dp),
                elevation = 4.dp
            ) {
                val accountFormUIState: AccountFormUIState = accountFormViewModel.accountFormUIState
                val imageUri: Uri? = accountFormUIState.imageUri
                val bitmap: Bitmap? = accountFormUIState.bitmap
                val context = LocalContext.current
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
                            .padding(4.dp)
                            .clip(CircleShape)
                    )
                }

                bitmap?.let { btm ->
                    Image(
                        bitmap = btm.asImageBitmap(),
                        contentDescription = "Profile Image",
                        alignment = Alignment.TopCenter,
                        modifier = Modifier
                            .wrapContentWidth()
                            .size(100.dp)
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
            Text("Producer's Name")
        }

    }
}
