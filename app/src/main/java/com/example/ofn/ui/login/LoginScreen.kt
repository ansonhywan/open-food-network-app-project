package com.example.ofn.presentation.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ofn.R
import com.example.ofn.presentation.components.FormTextField
import com.example.ofn.presentation.navigation.HOME_GRAPH_ROUTE
import com.example.ofn.presentation.navigation.Screen

@Composable
fun LoginScreen(navController: NavController, loginFormViewModel: LoginFormViewModel) {
    val username:String by loginFormViewModel.username.observeAsState("")
    val password:String by loginFormViewModel.password.observeAsState("")
    val rememberMe:Boolean by loginFormViewModel.rememberMe.observeAsState(false)
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    Surface{
        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Login",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(16.dp),
                fontSize = 40.sp
            )
            Spacer(modifier = Modifier.size(16.dp))
            FormTextField(
                text = username,
                onValueChange = { loginFormViewModel.onUsernameChange(it) },
                label = { Text(text = "UserName") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_person_24),
                        contentDescription = "UserName Icon"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.medium,
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                visualTransformation = VisualTransformation.None
            )
            Spacer(modifier = Modifier.size(10.dp))
            FormTextField(
                text = password,
                onValueChange = { loginFormViewModel.onPasswordChange(it) },
                label = { Text(text = "Password") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_lock_24),
                        contentDescription = "Password Icon"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation(),
                shape = MaterialTheme.shapes.medium,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
            )
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Checkbox(checked = rememberMe, onCheckedChange = { loginFormViewModel.onRememberMeChange(it)})
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(text = "Remember me")
                }

                TextButton(
                    onClick = { /*TODO*/ },
                ) {
                    Text(text = "Forgot Password")
                }
            }
            Button(
                onClick = {
                    navController.navigate(HOME_GRAPH_ROUTE)
                    Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Login")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Don't have an Account?")
            TextButton(
                onClick = { navController.navigate(Screen.Signup.route) },
            ) {
                Text(text = "Sign Up")
            }
        }
    }
}