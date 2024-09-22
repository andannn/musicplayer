package com.andanana.melodify.feature.player.widget

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.andanana.melodify.core.designsystem.theme.DynamicThemePrimaryColorsFromImage
import com.andanana.melodify.core.designsystem.theme.MinContrastOfPrimaryVsSurface
import com.andanana.melodify.core.designsystem.theme.rememberDominantColorState
import com.andanana.melodify.core.designsystem.util.contrastAgainst
import com.andanana.melodify.feature.player.PlayState
import com.andanana.melodify.feature.player.PlayerUiEvent
import com.andanana.melodify.feature.player.PlayerUiState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShrinkablePlayBox(
    state: PlayerUiState.Active,
    modifier: Modifier = Modifier,
    onEvent: (PlayerUiEvent) -> Unit,
) {
    val navigationBarHeight = WindowInsets.navigationBars.getBottom(LocalDensity.current)
    val statusBarHeight = WindowInsets.statusBars.getTop(LocalDensity.current)
    val animaScope = rememberCoroutineScope()
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
    ) {
        val surfaceColor = MaterialTheme.colorScheme.surface
        val dominantColorState =
            rememberDominantColorState { color ->
                // We want a color which has sufficient contrast against the surface color
                color.contrastAgainst(surfaceColor) >= MinContrastOfPrimaryVsSurface
            }

        val url = state.mediaItem.artWorkUri

        val layoutState: PlayerLayoutState =
            remember(density, maxHeight, navigationBarHeight, statusBarHeight) {
                PlayerLayoutState(
                    animaScope = animaScope,
                    screenHeight = constraints.maxHeight,
                    navigationBarHeight = navigationBarHeight,
                    statusBarHeight = statusBarHeight,
                    density = density,
                )
            }

        val isPlayerDraggable by
            remember {
                derivedStateOf {
                    !layoutState.isSheetExpanding
                }
            }

        BackHandler(
            enabled = layoutState.playerState == PlayerState.Expand,
            layoutState::shrinkPlayerLayout,
        )

        DynamicThemePrimaryColorsFromImage(dominantColorState) {
            // When the selected image url changes, call updateColorsFromImageUrl() or reset()
            LaunchedEffect(url, layoutState.isPlayerExpanding) {
                if (layoutState.isPlayerExpanding) {
                    dominantColorState.updateColorsFromImageUrl(url.toString())
                } else {
                    dominantColorState.reset()
                }
            }

            FlexiblePlayerLayout(
                modifier =
                    Modifier
                        .height(with(LocalDensity.current) { layoutState.playerExpandState.offset.toDp() })
                        .align(Alignment.BottomCenter)
                        .anchoredDraggable(
                            layoutState.playerExpandState,
                            enabled = isPlayerDraggable,
                            orientation = Orientation.Vertical,
                            reverseDirection = true,
                        )
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            enabled = layoutState.playerState == PlayerState.Shrink,
                            onClick = layoutState::expandPlayerLayout,
                        ),
                layoutState = layoutState,
                coverUri = state.mediaItem.artWorkUri,
                playMode = state.playMode,
                isShuffle = state.isShuffle,
                isPlaying = state.state == PlayState.PLAYING,
                isFavorite = state.isFavorite,
                playListQueue = state.playListQueue,
                activeMediaItem = state.mediaItem,
                title = state.mediaItem.name,
                artist = state.mediaItem.artist,
                progress = state.progress,
                onShrinkButtonClick = layoutState::shrinkPlayerLayout,
                onEvent = onEvent,
            )
        }
    }
}
