package com.example.ofn.ui.navigation

import com.example.ofn.R

const val ROOT_GRAPH_ROUTE = "root"
const val AUTH_GRAPH_ROUTE = "auth"
const val HOME_GRAPH_ROUTE = "home"
const val SETTING_GRAPH_ROUTE = "setting"

sealed class Screen(var title:String, var icon:Int?, var route:String){
    object Dashboard : Screen("Dashboard", R.drawable.home,"dashboard_screen")
    object Inventory: Screen("Inventory", R.drawable.bag,"inventory_screen")
    object Platform: Screen("Platform", R.drawable.coin,"platform_screen")
    object Settings: Screen("Settings", R.drawable.settings,"settings_screen")
    object Account: Screen("Account", R.drawable.account,"account_screen")
    object ManageProduct: Screen("Manage", null,"manage_screen")
    object ManageProductsAndCategories: Screen("ManageProductsAndCategories", null,"ManageProductsAndCategories_screen")
    object Login: Screen("Login", null,"login_screen")
    object Signup: Screen("Signup", null,"signup_screen")
    object ManageSpecifiedProduct: Screen("Manage/productName={productName}?category={category}", null, "help")
}
