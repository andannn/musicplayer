package com.andanana.musicplayer.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColorScheme(
)

private val LightColorPalette = lightColorScheme(
    primary = DarkBlue400,
    onPrimary = DarkBlue400,
    secondary = DarkRed600,
    onSecondary = DarkRed200,
    surface = DarkBlue50,
    onSurface = DarkBlue900,
    onSurfaceVariant = DarkBlue900
)

@Composable
fun MusicPlayerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
