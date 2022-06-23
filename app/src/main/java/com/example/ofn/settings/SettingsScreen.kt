package com.example.ofn.settings

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ofn.MainApplication
import com.example.ofn.R
import com.example.ofn.Screen
import com.example.ofn.components.NavigationPanel
import com.example.ofn.ui.theme.OFNTheme

@Composable
fun SettingsScreen(navController: NavController?) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.LightGray,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            ProfileCard()
            Column(
                modifier = Modifier
                    .padding(7.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.Start
            ) {
                NavigationPanel(
                    text = "Edit Account"
                ) {
                    navController?.navigate(Screen.Account.route)
                }
                NavigationPanel(
                    text = "Manage Product & Categories"
                ) {
                    navController?.navigate(Screen.Manage.route)
                }
            }

        }
    }
}


@Composable
fun ProfileCard() {
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
                Image(
                    painter = painterResource(R.drawable.farmers),
                    modifier = Modifier.size(72.dp),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Content description"
                )
            }
            Text("Producer's Name")
        }

    }
}
@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    SettingsScreen(navController = null)
}