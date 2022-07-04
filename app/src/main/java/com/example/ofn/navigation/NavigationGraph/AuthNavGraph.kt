package com.example.ofn.navigation.NavigationGraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.ofn.login.LoginFormViewModel
import com.example.ofn.login.LoginScreen
import com.example.ofn.navigation.AUTH_GRAPH_ROUTE
import com.example.ofn.navigation.Screen
import com.example.ofn.signup.SignupFormViewModel
import com.example.ofn.signup.SignupScreen

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