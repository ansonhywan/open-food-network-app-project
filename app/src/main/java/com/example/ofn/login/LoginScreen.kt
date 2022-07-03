package com.example.ofn.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ofn.components.FormTextField
import com.example.ofn.navigation.HOME_GRAPH_ROUTE
import com.example.ofn.navigation.Screen

@Composable
fun LoginScreen(navController: NavController, loginFormViewModel: LoginFormViewModel) {
    val name:String by loginFormViewModel.name.observeAsState("")
    val email:String by loginFormViewModel.email.observeAsState("")
    val phone:String by loginFormViewModel.phone.observeAsState("")
    val focusManager = LocalFocusManager.current
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(
                "Login",
                fontSize = 40.sp
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Name")
                    Icon(Icons.Outlined.Person, contentDescription = "Name Icon")
                    FormTextField(
                        text = name,
                        placeholder = "Name",
                        onChange = { loginFormViewModel.onNameChange(it) },
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
                    Text("Phone")
                    Icon(Icons.Outlined.Phone, contentDescription = "Phone Icon")
                    FormTextField(
                        text = phone,
                        placeholder = "Phone",
                        onChange = { loginFormViewModel.onPhoneChange(it) },
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
                    Text("Email")
                    Icon(Icons.Outlined.Email, contentDescription = "Email Icon")
                    FormTextField(
                        text = email,
                        placeholder = "Email",
                        onChange = { loginFormViewModel.onEmailChange(it) },
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
                    Button(onClick = { navController.navigate(Screen.Signup.route) }) {
                        Text(text = "Sign Up")
                    }
                    Button(onClick = { navController.navigate(HOME_GRAPH_ROUTE) }) {
                        Text(text = "Log In Success and go to Home")
                    }
                }
            }
        }
    }
}