package com.example.ofn.presentation.navigation.NavigationGraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.ofn.presentation.dashboard.DashboardScreen
import com.example.ofn.presentation.inventory.InventoryScreen
import com.example.ofn.presentation.navigation.HOME_GRAPH_ROUTE
import com.example.ofn.presentation.navigation.Screen
import com.example.ofn.presentation.platform.PlatformScreen
import com.example.ofn.presentation.settings.SettingsScreen
import com.example.ofn.presentation.settings.account.AccountFormViewModel
import com.example.ofn.presentation.settings.manage.ManageViewModel

fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController,
    accountFormViewModel: AccountFormViewModel,
    manageViewModel: ManageViewModel
){
    navigation(
        startDestination = Screen.Dashboard.route,
        route = HOME_GRAPH_ROUTE,
    ){
        composable(Screen.Dashboard.route) { DashboardScreen(navController) }
        composable(Screen.Inventory.route) { InventoryScreen(navController) }
        composable(Screen.Platform.route) { PlatformScreen(navController) }
        composable(Screen.Settings.route) { SettingsScreen(navController, accountFormViewModel) }
        settingNavGraph(navController, accountFormViewModel, manageViewModel)
    }
}