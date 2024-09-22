package com.andanana.melodify.feature.player

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.andanana.melodify.core.designsystem.theme.DynamicThemePrimaryColorsFromImage
import com.andanana.melodify.core.designsystem.theme.MinContrastOfPrimaryVsSurface
import com.andanana.melodify.core.designsystem.theme.rememberDominantColorState
import com.andanana.melodify.core.designsystem.util.contrastAgainst
import com.andanana.melodify.feature.player.widget.ShrinkablePlayBox

@Composable
fun PlayerSheet(
    state: PlayerUiState.Active,
    onEvent: (PlayerUiEvent) -> Unit,
) {
    val surfaceColor = MaterialTheme.colorScheme.surface
    val dominantColorState =
        rememberDominantColorState { color ->
            // We want a color which has sufficient contrast against the surface color
            color.contrastAgainst(surfaceColor) >= MinContrastOfPrimaryVsSurface
        }

    val url = state.mediaItem.artWorkUri

    DynamicThemePrimaryColorsFromImage(dominantColorState) {
        // When the selected image url changes, call updateColorsFromImageUrl() or reset()
        LaunchedEffect(url) {
            if (url.isEmpty()) {
                dominantColorState.updateColorsFromImageUrl(url)
            } else {
                dominantColorState.reset()
            }
        }

        ShrinkablePlayBox(
            state = state,
            onEvent = onEvent,
        )
    }
}
