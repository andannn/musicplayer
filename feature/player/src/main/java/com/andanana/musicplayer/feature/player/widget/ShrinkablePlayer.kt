package com.andanana.musicplayer.feature.player.widget

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.andanana.musicplayer.feature.player.PlayState
import com.andanana.musicplayer.feature.player.PlayerUiEvent
import com.andanana.musicplayer.feature.player.PlayerUiState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShrinkablePlayBox(
    modifier: Modifier = Modifier,
    state: PlayerUiState.Active,
    onEvent: (PlayerUiEvent) -> Unit,
) {
    val navigationBarHeight = WindowInsets.navigationBars.getBottom(LocalDensity.current)
    val statusBarHeight = WindowInsets.statusBars.getTop(LocalDensity.current)
    val animaScope = rememberCoroutineScope()
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
    ) {
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
            coverUri = state.mediaItem.mediaMetadata.artworkUri.toString(),
            playMode = state.playMode,
            isShuffle = state.isShuffle,
            isPlaying = state.state == PlayState.PLAYING,
            isFavorite = state.isFavorite,
            title = state.mediaItem.mediaMetadata.title.toString(),
            artist = state.mediaItem.mediaMetadata.artist.toString(),
            progress = state.progress,
            onShrinkButtonClick = layoutState::shrinkPlayerLayout,
            onEvent = onEvent,
        )
    }
}
