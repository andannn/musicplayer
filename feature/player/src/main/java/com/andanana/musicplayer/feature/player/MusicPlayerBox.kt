package com.andanana.musicplayer.feature.player

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

private const val TAG = "MiniPlayerBox"

private val PlayerShrinkHeight = 70.dp
private const val SwipeGestureVelocityThreshold = 1000

private enum class PlayBoxState {
    SHRINK,
    EXPAND,
    SHRINKING,
    EXPANDING,
    ;

    fun isExpand() = this == EXPANDING || this == EXPAND
}

@Composable
fun MusicPlayerBox(
    playerStateViewModel: PlayerStateViewModel = hiltViewModel(),
    onNavigateToPlayer: () -> Unit,
    onToggleFavorite: (Uri) -> Unit,
) {
    val state by playerStateViewModel.playerUiStateFlow.collectAsState()

    if (state is PlayerUiState.Active) {
        ShrinkablePlayBox(
            state = state as PlayerUiState.Active,
            onPlayerSheetClick = onNavigateToPlayer,
            onPlayControlButtonClick = playerStateViewModel::togglePlayState,
            onFavoriteButtonClick = {},
            onPlayNextButtonClick = playerStateViewModel::next,
        )
    }
}

@Composable
private fun ShrinkablePlayBox(
    modifier: Modifier = Modifier,
    state: PlayerUiState.Active,
    onPlayerSheetClick: () -> Unit = {},
    onPlayControlButtonClick: () -> Unit = {},
    onFavoriteButtonClick: () -> Unit = {},
    onPlayNextButtonClick: () -> Unit = {},
) {
    val navigationBarHeight = WindowInsets.navigationBars.getBottom(LocalDensity.current)
    val minHeight =
        with(LocalDensity.current) {
            PlayerShrinkHeight.toPx()
        }
    val animaScope = rememberCoroutineScope()

    val systemUiController = rememberSystemUiController()
    val statusBarColor = MaterialTheme.colorScheme.surface
    val isDarkTheme = isSystemInDarkTheme()

    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
    ) {
        val maxHeight = constraints.maxHeight.toFloat()
        val verticalBias = 1 - (2 * navigationBarHeight).toFloat().div(maxHeight - minHeight)

        var currentState by
            remember {
                mutableStateOf(PlayBoxState.SHRINK)
            }

        var playerHeight by
            remember {
                mutableFloatStateOf(minHeight)
            }

        val animation =
            remember(minHeight, maxHeight) {
                Animatable(minHeight).apply {
                    updateBounds(minHeight, maxHeight)
                }
            }

        suspend fun expandAnimation() {
            Log.d(TAG, "expandAnimation: E")
            currentState = PlayBoxState.EXPANDING
            animation.snapTo(playerHeight)
            animation.animateTo(
                targetValue = maxHeight,
                animationSpec = TweenSpec(durationMillis = 300, easing = EaseIn),
            ) {
                Log.d(TAG, "Animation effecting the player height $playerHeight")
                playerHeight = this.value
            }
            currentState = PlayBoxState.EXPAND
            Log.d(TAG, "expandAnimation: X $maxHeight")
        }

        suspend fun shrinkAnimation() {
            Log.d(TAG, "shrinkAnimation: E")
            currentState = PlayBoxState.SHRINKING
            animation.snapTo(playerHeight)
            animation.animateTo(
                minHeight,
                animationSpec = TweenSpec(durationMillis = 300, easing = EaseOut),
            ) {
                Log.d(TAG, "Animation effecting the player height $playerHeight")
                playerHeight = this.value
            }
            currentState = PlayBoxState.SHRINK
            Log.d(TAG, "shrinkAnimation: X")
        }

        fun animatedToggle() {
            animaScope.launch {
                animation.stop()

                if (currentState == PlayBoxState.SHRINK || currentState == PlayBoxState.SHRINKING) {
                    expandAnimation()
                } else if (currentState == PlayBoxState.EXPAND || currentState == PlayBoxState.EXPANDING) {
                    shrinkAnimation()
                }
            }
        }

        val draggableState =
            rememberDraggableState { delta ->
                playerHeight -= delta
            }

        val draggable =
            Modifier.draggable(
                draggableState,
                orientation = Orientation.Vertical,
                onDragStarted = {
                    animation.stop()
                },
                onDragStopped = { velocity ->
                    val isSwipe = velocity.absoluteValue > SwipeGestureVelocityThreshold
                    val isUp = velocity < 0
                    Log.d(TAG, "MiniPlayerBox: onDragStopped. velocity: $velocity")
                    Log.d(TAG, "MiniPlayerBox: onDragStopped. isSwipe: $isSwipe")
                    Log.d(TAG, "MiniPlayerBox: onDragStopped. isUp: $isUp")
                    if (isSwipe) {
                        if (isUp) {
                            // Start expand animation.
                            expandAnimation()
                        } else {
                            // Start shrink animation.
                            shrinkAnimation()
                        }
                    } else {
                        val isCurrentPlayerUpperArea = playerHeight >= maxHeight.div(2)
                        if (isCurrentPlayerUpperArea) {
                            // Start expand animation.
                            expandAnimation()
                        } else {
                            // Start shrink animation.
                            shrinkAnimation()
                        }
                    }
                },
            )

        BackHandler(enabled = currentState.isExpand()) {
            if (currentState.isExpand()) {
                animatedToggle()
            }
        }

        DisposableEffect(currentState.isExpand()) {
            if (currentState.isExpand()) {
                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = !isDarkTheme)
            } else {
                systemUiController.setSystemBarsColor(color = statusBarColor)
            }
            onDispose {
                systemUiController.setSystemBarsColor(color = statusBarColor)
            }
        }

        FlexiblePlayerLayout(
            modifier =
                Modifier
                    .height(with(LocalDensity.current) { playerHeight.toDp() })
                    .align(BiasAlignment(0f, verticalBias))
                    .then(draggable)
                    .clickable(
                        enabled = currentState == PlayBoxState.SHRINK,
                    ) {
                        animatedToggle()
                    },
            heightPxRange = minHeight..maxHeight,
            coverUri = state.mediaItem.mediaMetadata.artworkUri.toString(),
            isPlaying = state.state == PlayState.PLAYING,
            isFavorite = state.isFavorite,
            title = state.mediaItem.mediaMetadata.title.toString(),
            artist = state.mediaItem.mediaMetadata.artist.toString(),
            progress = state.progress,
            onPlayerSheetClick = onPlayerSheetClick,
            onPlayControlButtonClick = onPlayControlButtonClick,
            onPlayNextButtonClick = onPlayNextButtonClick,
            onFavoriteButtonClick = onFavoriteButtonClick,
        )
    }
}
