package com.example.ofn.ui.navigation.NavigationGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.ofn.ui.navigation.SETTING_GRAPH_ROUTE
import com.example.ofn.ui.navigation.Screen
import com.example.ofn.ui.settings.account.AccountScreen
import com.example.ofn.ui.settings.ManageProductsAndCategories.ManageProductsAndCategoriesScreen
import com.example.ofn.ui.settings.ManageProductsAndCategories.ManageProductsAndCategoriesViewModel
import com.example.ofn.ui.settings.manage.ManageScreen
import com.example.ofn.ui.settings.account.AccountFormViewModel
import com.example.ofn.ui.settings.manage.ManageViewModel

fun NavGraphBuilder.settingNavGraph(
    navController: NavHostController,
    accountFormViewModel: AccountFormViewModel,
    manageViewModel: ManageViewModel,
    manageProductsAndCategoriesViewModel: ManageProductsAndCategoriesViewModel
){
    navigation(
        startDestination = Screen.Account.route,
        route = SETTING_GRAPH_ROUTE
    ){
        composable(Screen.Account.route) { AccountScreen(navController, accountFormViewModel) }
        composable(Screen.ManageProductsAndCategories.route) { ManageProductsAndCategoriesScreen(navController, manageProductsAndCategoriesViewModel) }
        composable(Screen.ManageProduct.route) { ManageScreen(navController, manageViewModel) }
    }
}