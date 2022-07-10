package com.example.ofn.presentation.navigation.NavigationGraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.ofn.presentation.login.LoginFormViewModel
import com.example.ofn.presentation.login.LoginScreen
import com.example.ofn.presentation.navigation.AUTH_GRAPH_ROUTE
import com.example.ofn.presentation.navigation.Screen
import com.example.ofn.presentation.signup.SignupFormViewModel
import com.example.ofn.presentation.signup.SignupScreen

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    loginFormViewModel: LoginFormViewModel,
    signupFormViewModel: SignupFormViewModel
){
    navigation(
        startDestination = Screen.Login.route,
        route = AUTH_GRAPH_ROUTE
    ){
        composable(
            route = Screen.Login.route
        ) {
            LoginScreen(navController = navController, loginFormViewModel)
        }
        composable(
            route = Screen.Signup.route
        ) {
            SignupScreen(navController = navController, signupFormViewModel)
        }
    }
}