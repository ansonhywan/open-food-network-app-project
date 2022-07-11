package com.example.ofn.ui.navigation.NavigationGraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.ofn.ui.dashboard.DashboardScreen
import com.example.ofn.ui.inventory.InventoryScreen
import com.example.ofn.ui.navigation.HOME_GRAPH_ROUTE
import com.example.ofn.ui.navigation.Screen
import com.example.ofn.ui.platform.PlatformScreen
import com.example.ofn.ui.settings.SettingsScreen
import com.example.ofn.ui.settings.account.AccountFormViewModel
import com.example.ofn.ui.settings.manage.ManageViewModel

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