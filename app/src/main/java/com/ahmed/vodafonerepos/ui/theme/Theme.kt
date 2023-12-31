package com.ahmed.vodafonerepos.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = White,
    primaryVariant = White,
    secondary = Black,
    onPrimary = White,
    onSurface = Black,
    background = Black,
    surface = Black,
    onSecondary = White,
    onBackground = White,
    secondaryVariant = nightGray
)

private val LightColorPalette = lightColors(
    primary = Black,
    primaryVariant = Black,
    secondary = White,
    onPrimary = Black,
    onSurface = White,
    background = White,
    surface = White,
    onSecondary = Black,
    onBackground = Black,
    secondaryVariant = Gray
)

@Composable
fun VodafoneReposTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
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