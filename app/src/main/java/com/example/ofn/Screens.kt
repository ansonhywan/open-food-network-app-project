package com.example.ofn

sealed class Screen(var title:String, var icon:Int, var route:String){
    object Dashboard : Screen("Dashboard", R.drawable.home,"dashboard")
    object Inventory: Screen("Inventory",R.drawable.bag,"inventory")
    object Platform: Screen("Platform",R.drawable.coin,"platform")
    object Settings: Screen("Settings",R.drawable.settings,"settings")
    object Account: Screen("Account",R.drawable.account,"account")
}
