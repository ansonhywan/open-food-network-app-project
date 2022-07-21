package com.example.ofn.ui.navigation.NavigationGraph
import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
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
        composable(Screen.ManageProductsAndCategories.route) {
            ManageProductsAndCategoriesScreen(navController, manageProductsAndCategoriesViewModel)
        }
        composable(Screen.ManageProduct.route) {
            val manageViewModel:ManageViewModel = com.example.ofn.ui.settings.manage.ManageViewModel(
                "",
                ""
            )
            ManageScreen(navController, manageViewModel) }
        composable(
            route=Screen.ManageSpecifiedProduct.route,
            arguments = listOf(
                navArgument("productName") {
                    // Make argument type safe
                    type = NavType.StringType
                } ,
                navArgument("category") {
                    // Make argument type safe
                    type = NavType.StringType
                }
            )
        ) {
            val manageViewModel: ManageViewModel? = it.arguments?.getString("productName")?.let { it1 ->
                com.example.ofn.ui.settings.manage.ManageViewModel(
                    it1,
                    it.arguments?.getString("category")!!
                )
            }
            if (manageViewModel != null) {
                ManageScreen(navController, manageViewModel)
                Log.d("Settings", manageViewModel.toString())
            }
        }
    }
}