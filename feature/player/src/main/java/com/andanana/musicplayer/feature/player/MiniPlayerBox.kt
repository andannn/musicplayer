package com.andanana.musicplayer.feature.player

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andanana.musicplayer.core.designsystem.component.MiniPlayerBox
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

private const val TAG = "MiniPlayerBox"

private val PlayerShrinkHeight = 70.dp

private enum class PlayBoxState {
    SHRINK,
    EXPAND,
    SHRINKING,
    EXPANDING,
    ;

    fun isExpand() = this == EXPANDING || this == EXPAND
}

@Composable
fun MiniPlayerBox(
    playerStateViewModel: PlayerStateViewModel = hiltViewModel(),
    onNavigateToPlayer: () -> Unit,
    onToggleFavorite: (Uri) -> Unit,
) {
    val playerUiState by playerStateViewModel.playerUiStateFlow.collectAsState()

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
        modifier = Modifier.fillMaxSize(),
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

        LaunchedEffect(animation.isRunning) {
            snapshotFlow {
                animation.value
            }.collect {
                if (animation.isRunning) {
                    Log.d(TAG, "Animation effecting the player height $it")
                    playerHeight = it
                }
            }
        }

        fun animatedToggle() {
            animaScope.launch {
                animation.stop()
                animation.snapTo(playerHeight)

                Log.d(TAG, "animatedToggle: E $currentState")
                if (currentState == PlayBoxState.SHRINK || currentState == PlayBoxState.SHRINKING) {
                    currentState = PlayBoxState.EXPANDING
                    animation.animateTo(maxHeight, animationSpec = TweenSpec(durationMillis = 300, easing = EaseIn))
                    currentState = PlayBoxState.EXPAND
                } else if (currentState == PlayBoxState.EXPAND || currentState == PlayBoxState.EXPANDING) {
                    currentState = PlayBoxState.SHRINKING
                    animation.animateTo(minHeight, animationSpec = TweenSpec(durationMillis = 300, easing = EaseOut))
                    currentState = PlayBoxState.SHRINK
                }
                Log.d(TAG, "animatedToggle: X $currentState")
            }
        }

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

        MiniPlayerBoxContent(
            modifier =
                Modifier
                    .height(with(LocalDensity.current) { playerHeight.toDp() })
                    .align(BiasAlignment(0f, verticalBias))
                    .clickable {
                        animatedToggle()
                    },
            state = playerUiState,
            onPlayerSheetClick = onNavigateToPlayer,
            onPlayControlButtonClick = playerStateViewModel::togglePlayState,
            onFavoriteButtonClick = {},
            onPlayNextButtonClick = playerStateViewModel::next,
        )
    }
}

@Composable
private fun MiniPlayerBoxContent(
    modifier: Modifier = Modifier,
    state: PlayerUiState,
    onPlayerSheetClick: () -> Unit = {},
    onPlayControlButtonClick: () -> Unit = {},
    onFavoriteButtonClick: () -> Unit = {},
    onPlayNextButtonClick: () -> Unit = {},
) {
    if (state is PlayerUiState.Active) {
        MiniPlayerBox(
            modifier = modifier,
            coverUri = state.mediaItem.mediaMetadata.artworkUri.toString(),
            isPlaying = state.state == PlayState.PLAYING,
            title = state.mediaItem.mediaMetadata.title.toString(),
            artist = state.mediaItem.mediaMetadata.artist.toString(),
            progress = state.progress,
            isFavorite = state.isFavorite,
            onPlayerSheetClick = onPlayerSheetClick,
            onPlayNextButtonClick = onPlayNextButtonClick,
            onPlayControlButtonClick = onPlayControlButtonClick,
            onFavoriteButtonClick = onFavoriteButtonClick,
        )
    }
}
