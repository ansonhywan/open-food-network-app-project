package com.example.ofn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ofn.data.repository.AuthRepository
import com.example.ofn.ui.theme.OFNTheme
import com.example.ofn.presentation.navigation.NavigationGraph.SetupNavGraph
import com.example.ofn.ui.navigation.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            OFNTheme {
                MainApplication()
            }
        }
    }
}

@Composable
fun MainApplication(){

    val screens = listOf<Screen>(
        Screen.Dashboard,
        Screen.Inventory,
        Screen.Platform,
        Screen.Settings
    )
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    if (currentDestination?.route != Screen.Login.route && currentDestination?.route != Screen.Signup.route){
        Scaffold(
            bottomBar = {
                BottomNavigation {
                    screens.forEach { screen ->
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    painterResource(id = screen.icon!!),
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
            SetupNavGraph(navController, Modifier.padding(innerPadding))
        }
    }else{
        SetupNavGraph(navController, Modifier.padding(10.dp))
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OFNTheme {
        MainApplication()
    }
}