package com.example.ofn.presentation.navigation.NavigationGraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.ofn.ui.login.LoginFormViewModel
import com.example.ofn.ui.navigation.AUTH_GRAPH_ROUTE
import com.example.ofn.ui.navigation.ROOT_GRAPH_ROUTE
import com.example.ofn.ui.settings.account.AccountFormViewModel
import com.example.ofn.ui.settings.manage.ManageViewModel
import com.example.ofn.ui.signup.SignupFormViewModel
import com.example.ofn.ui.navigation.NavigationGraph.authNavGraph
import com.example.ofn.ui.navigation.NavigationGraph.homeNavGraph

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    modifier: Modifier
) {
    val accountFormViewModel: AccountFormViewModel = viewModel()
    val manageViewModel: ManageViewModel = viewModel()
    val loginFormViewModel: LoginFormViewModel = viewModel()
    val signupFormViewModel: SignupFormViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = AUTH_GRAPH_ROUTE,
        route = ROOT_GRAPH_ROUTE,
        modifier = modifier
    ) {
        homeNavGraph(navController = navController, accountFormViewModel, manageViewModel)
        authNavGraph(navController = navController, loginFormViewModel, signupFormViewModel)
    }
}