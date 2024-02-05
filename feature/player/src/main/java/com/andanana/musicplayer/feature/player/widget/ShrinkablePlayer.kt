package com.andanana.musicplayer.feature.player.widget

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.andanana.musicplayer.feature.player.PlayState
import com.andanana.musicplayer.feature.player.PlayerUiEvent
import com.andanana.musicplayer.feature.player.PlayerUiState
import kotlinx.coroutines.launch

enum class ShrinkablePlayerState { Shrink, Expand }

val PlayerShrinkHeight = 70.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShrinkablePlayBox(
    modifier: Modifier = Modifier,
    state: PlayerUiState.Active,
    onEvent: (PlayerUiEvent) -> Unit,
) {
    val navigationBarHeight = WindowInsets.navigationBars.getBottom(LocalDensity.current)
    val minHeight =
        with(LocalDensity.current) { PlayerShrinkHeight.toPx() } + navigationBarHeight
    val animaScope = rememberCoroutineScope()
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
    ) {
        val maxHeight = constraints.maxHeight.toFloat()

        val anchors =
            DraggableAnchors {
                ShrinkablePlayerState.Shrink at minHeight
                ShrinkablePlayerState.Expand at maxHeight
            }

        val anchoredDraggableState =
            remember {
                AnchoredDraggableState(
                    initialValue = ShrinkablePlayerState.Shrink,
                    anchors = anchors,
                    positionalThreshold = { with(density) { 26.dp.toPx() } },
                    velocityThreshold = { with(density) { 20.dp.toPx() } },
                    animationSpec = spring(),
                )
            }

        val currentState by
            derivedStateOf {
                anchoredDraggableState.currentValue
            }

        fun onShrinkButtonClick() {
            animaScope.launch {
                anchoredDraggableState.animateTo(ShrinkablePlayerState.Shrink)
            }
        }

        BackHandler(enabled = currentState == ShrinkablePlayerState.Expand) {
            animaScope.launch {
                anchoredDraggableState.animateTo(ShrinkablePlayerState.Shrink)
            }
        }

        FlexiblePlayerLayout(
            modifier =
                Modifier
                    .height(with(LocalDensity.current) { anchoredDraggableState.offset.toDp() })
                    .align(Alignment.BottomCenter)
                    .anchoredDraggable(
                        anchoredDraggableState,
                        orientation = Orientation.Vertical,
                        reverseDirection = true,
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        enabled = currentState == ShrinkablePlayerState.Shrink,
                        onClick = {
                            animaScope.launch {
                                anchoredDraggableState.animateTo(ShrinkablePlayerState.Expand)
                            }
                        },
                    ),
            heightPxRange = minHeight..maxHeight,
            coverUri = state.mediaItem.mediaMetadata.artworkUri.toString(),
            isPlaying = state.state == PlayState.PLAYING,
            isFavorite = state.isFavorite,
            title = state.mediaItem.mediaMetadata.title.toString(),
            artist = state.mediaItem.mediaMetadata.artist.toString(),
            progress = state.progress,
            onShrinkButtonClick = ::onShrinkButtonClick,
            onEvent = onEvent,
        )
    }
}
