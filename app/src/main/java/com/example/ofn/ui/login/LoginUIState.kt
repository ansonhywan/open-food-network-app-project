package com.example.ofn.ui.login

data class LoginUIState(
    val email:String = "",
    val password: String = "",
    val rememberMe: Boolean = false
)