package com.example.ofn.navigation.NavigationGraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.ofn.login.LoginFormViewModel
import com.example.ofn.navigation.AUTH_GRAPH_ROUTE
import com.example.ofn.navigation.HOME_GRAPH_ROUTE
import com.example.ofn.navigation.ROOT_GRAPH_ROUTE
import com.example.ofn.settings.account.AccountFormViewModel
import com.example.ofn.settings.manage.ManageViewModel
import com.example.ofn.signup.SignupFormViewModel

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    modifier: Modifier
) {
    val scope = rememberCoroutineScope()
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
        homeNavGraph(navController = navController, accountFormViewModel, manageViewModel, scope)
        authNavGraph(navController = navController, loginFormViewModel, signupFormViewModel)
    }
}