package com.andannn.melodify.feature.common.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorPalette = darkColorScheme()

private val LightColorPalette = lightColorScheme()

@Composable
actual fun MelodifyTheme(
    darkTheme: Boolean,
    isDynamicColor: Boolean,
    content: @Composable () -> Unit
) {

    val dynamicColor = isDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colorScheme =
        when {
            dynamicColor && darkTheme -> {
                dynamicDarkColorScheme(LocalContext.current)
            }

            dynamicColor && !darkTheme -> {
                dynamicLightColorScheme(LocalContext.current)
            }

            darkTheme -> DarkColorPalette
            else -> LightColorPalette
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
