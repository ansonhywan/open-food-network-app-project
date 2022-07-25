package com.example.ofn.presentation.navigation.NavigationGraph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.ofn.data.repository.AuthRepository
import com.example.ofn.ui.inventory.InventoryViewModel
import com.example.ofn.ui.login.LoginFormViewModel
import com.example.ofn.ui.navigation.AUTH_GRAPH_ROUTE
import com.example.ofn.ui.navigation.ROOT_GRAPH_ROUTE
import com.example.ofn.ui.settings.account.AccountFormViewModel
import com.example.ofn.ui.settings.manage.ManageViewModel
import com.example.ofn.ui.signup.SignupFormViewModel
import com.example.ofn.ui.navigation.NavigationGraph.authNavGraph
import com.example.ofn.ui.navigation.NavigationGraph.homeNavGraph
import com.example.ofn.ui.platform.PlatformViewModel
import com.example.ofn.ui.settings.ManageProductsAndCategories.ManageProductsAndCategoriesViewModel

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    modifier: Modifier
) {
    val inventoryViewModel: InventoryViewModel = viewModel()
    inventoryViewModel.populateCategories()
    val platformViewModel: PlatformViewModel = viewModel()
    val accountFormViewModel: AccountFormViewModel = viewModel()
    val manageViewModel: ManageViewModel = viewModel()
    manageViewModel.populateManageScreen()
    val loginFormViewModel: LoginFormViewModel = viewModel(modelClass = LoginFormViewModel::class.java)
    val signupFormViewModel: SignupFormViewModel = viewModel()
    val manageProductsAndCategoriesViewModel: ManageProductsAndCategoriesViewModel = viewModel()
    manageProductsAndCategoriesViewModel.populateCategories()
    NavHost(
        navController = navController,
        startDestination = AUTH_GRAPH_ROUTE,
        route = ROOT_GRAPH_ROUTE,
        modifier = modifier
    ) {
        homeNavGraph(navController = navController, inventoryViewModel, platformViewModel, accountFormViewModel, manageViewModel, manageProductsAndCategoriesViewModel)
        authNavGraph(navController = navController, loginFormViewModel, signupFormViewModel)
    }
}