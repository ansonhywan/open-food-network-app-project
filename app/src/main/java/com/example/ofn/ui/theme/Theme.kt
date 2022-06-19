package com.example.ofn.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    background = Color.Black,
    surface = Color.Black,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

private val LightColorPalette = lightColors(
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

@Composable
fun OFNTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
fun OFNButtonColors(darkTheme: Boolean = isSystemInDarkTheme()): ButtonColors {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    return buttonColors(
            backgroundColor = colors.onSecondary,
            contentColor = colors.onPrimary
        )
}
