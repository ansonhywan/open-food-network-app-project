package com.example.ofn.ui.navigation.NavigationGraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.ofn.data.repository.AuthRepository
import com.example.ofn.ui.dashboard.DashboardScreen
import com.example.ofn.ui.inventory.InventoryScreen
import com.example.ofn.ui.inventory.InventoryViewModel
import com.example.ofn.ui.navigation.HOME_GRAPH_ROUTE
import com.example.ofn.ui.navigation.Screen
import com.example.ofn.ui.platform.PlatformScreen
import com.example.ofn.ui.platform.PlatformViewModel
import com.example.ofn.ui.settings.ManageProductsAndCategories.ManageProductsAndCategoriesViewModel
import com.example.ofn.ui.settings.SettingsScreen
import com.example.ofn.ui.settings.account.AccountFormViewModel
import com.example.ofn.ui.settings.manage.ManageViewModel

fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController,
    inventoryViewModel: InventoryViewModel,
    platformViewModel: PlatformViewModel,
    accountFormViewModel: AccountFormViewModel,
    manageViewModel: ManageViewModel,
    manageProductsAndCategoriesViewModel: ManageProductsAndCategoriesViewModel
){
    navigation(
        startDestination = Screen.Inventory.route,
        route = HOME_GRAPH_ROUTE,
    ){
        composable(Screen.Dashboard.route) { DashboardScreen(navController) }
        composable(Screen.Inventory.route) { InventoryScreen(navController, inventoryViewModel) }
        composable(Screen.Platform.route) { PlatformScreen(navController, platformViewModel) }
        composable(Screen.Settings.route) { SettingsScreen(navController, accountFormViewModel) }
        settingNavGraph(navController, accountFormViewModel, manageViewModel, manageProductsAndCategoriesViewModel)
    }
}