/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andannn.melodify.feature.common.dynamic_theming

import androidx.collection.LruCache
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

@Composable
expect fun rememberDominantColorState(
    defaultColor: Color = MaterialTheme.colorScheme.primary,
    defaultOnColor: Color = MaterialTheme.colorScheme.onPrimary,
    cacheSize: Int = 12,
    isColorValid: (Color) -> Boolean = { true },
): DominantColorState


/**
 * A composable which allows dynamic theming of the primary
 * color from an image.
 */
@Composable
fun DynamicThemePrimaryColorsFromImage(
    dominantColorStateImpl: DominantColorState = rememberDominantColorState(),
    content: @Composable () -> Unit,
) {
    val defaultScheme = MaterialTheme.colorScheme
    var scheme: ColorScheme by remember { mutableStateOf(defaultScheme) }
    val seedColor by
    animateColorAsState(
        dominantColorStateImpl.color,
        spring(stiffness = Spring.StiffnessLow),
        finishedListener = {
            scheme = createThemeFromSeed(it, isDark = true)
        },
        label = "domain color",
    )

    var debounceCounter by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(seedColor) {
        debounceCounter += 1
        if (debounceCounter % 5 == 0) {
            scheme = createThemeFromSeed(seedColor, isDark = true)
        }
    }

    MaterialTheme(
        colorScheme = scheme,
        content = content
    )
}

expect class DominantColorState {
    val color: Color

    suspend fun updateColorsFromImageUrl(url: String)

    fun setDynamicThemeEnable(enable: Boolean)
}