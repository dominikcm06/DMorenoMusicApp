package com.example.dmorenomusicapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryPurple,
    secondary = LightLavender,
    background = LightLavender,
    surface = CardWhite,
    onPrimary = Color.White,
    onSecondary = PrimaryPurple,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun DMorenoMusicAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
