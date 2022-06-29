package com.example.ofn

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ofn.ui.theme.OFNTheme
import com.example.ofn.dashboard.DashboardScreen
import com.example.ofn.inventory.InventoryScreen
import com.example.ofn.platform.PlatformScreen
import com.example.ofn.settings.AccountScreen
import com.example.ofn.settings.ManageProductsAndCategoriesScreen
import com.example.ofn.settings.ManageScreen
import com.example.ofn.settings.SettingsScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ofn.settings.account.AccountFormViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OFNTheme {
                MainApplication()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun MainApplication(){
    val screens = listOf<Screen>(
        Screen.Dashboard,
        Screen.Inventory,
        Screen.Platform,
        Screen.Settings
    )
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val accountFormViewModel: AccountFormViewModel = viewModel()
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                screens.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                                Icon(
                                    painterResource(id = screen.icon),
                                    contentDescription = screen.title,
                                    modifier = Modifier.size(25.dp)
                                )
                               },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(screen.route)
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Dashboard.route, Modifier.padding(innerPadding)) {
            composable(Screen.Dashboard.route) { DashboardScreen(navController) }
            composable(Screen.Inventory.route) { InventoryScreen(navController) }
            composable(Screen.Platform.route) { PlatformScreen(navController) }
            composable(Screen.Settings.route) { SettingsScreen(navController, accountFormViewModel) }
            composable(Screen.Account.route) { AccountScreen(navController, accountFormViewModel, scope) }
            composable(Screen.ManageProductsAndCategories.route) { ManageProductsAndCategoriesScreen(
                navController = navController
            ) }
            composable(Screen.ManageProduct.route) { ManageScreen(navController = navController) }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OFNTheme {
        MainApplication()
    }
}