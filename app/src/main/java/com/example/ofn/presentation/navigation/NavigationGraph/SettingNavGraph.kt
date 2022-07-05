package com.example.ofn.presentation.navigation.NavigationGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.ofn.presentation.navigation.SETTING_GRAPH_ROUTE
import com.example.ofn.presentation.navigation.Screen
import com.example.ofn.settings.AccountScreen
import com.example.ofn.settings.ManageProductsAndCategoriesScreen
import com.example.ofn.settings.ManageScreen
import com.example.ofn.presentation.settings.account.AccountFormViewModel
import com.example.ofn.presentation.settings.manage.ManageViewModel

fun NavGraphBuilder.settingNavGraph(
    navController: NavHostController,
    accountFormViewModel: AccountFormViewModel,
    manageViewModel: ManageViewModel
){
    navigation(
        startDestination = Screen.Account.route,
        route = SETTING_GRAPH_ROUTE
    ){
        composable(Screen.Account.route) { AccountScreen(navController, accountFormViewModel) }
        composable(Screen.ManageProductsAndCategories.route) { ManageProductsAndCategoriesScreen(navController) }
        composable(Screen.ManageProduct.route) { ManageScreen(navController, manageViewModel) }
    }
}