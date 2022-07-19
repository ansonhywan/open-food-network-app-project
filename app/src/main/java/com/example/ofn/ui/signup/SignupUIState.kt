package com.example.ofn.ui.signup

data class SignupUIState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val rememberMe: Boolean = false
)