package com.andanana.musicplayer.feature.player.widget

import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class PlayerState { Shrink, Expand }

enum class BottomSheetState { Shrink, Expand }

val PlayerShrinkHeight = 70.dp

@Stable
@OptIn(ExperimentalFoundationApi::class)
class PlayerLayoutState(
    screenHeight: Int,
    val navigationBarHeight: Int,
    statusBarHeight: Int,
    density: Density,
    private val animaScope: CoroutineScope,
) {
    private val playerShrinkHeightPx = with(density) { PlayerShrinkHeight.toPx() }
    val sheetHeight =
        screenHeight - statusBarHeight - playerShrinkHeightPx
    private val bottomSheetDragAreaHeightPx = with(density) { BottomSheetDragAreaHeight.toPx() }

    private val sheetAnchors =
        DraggableAnchors {
            BottomSheetState.Shrink at sheetHeight - bottomSheetDragAreaHeightPx
            BottomSheetState.Expand at 0f
        }

    val sheetState =
        AnchoredDraggableState(
            initialValue = BottomSheetState.Shrink,
            anchors = sheetAnchors,
            positionalThreshold = { 300f },
            velocityThreshold = { 400f },
            snapAnimationSpec = spring(),
            decayAnimationSpec = exponentialDecay(),
        )

    // 1f when sheet shrink, 0f when sheet fully expanded.
    val sheetShrinkFactor by derivedStateOf {
        sheetState.offset.div(sheetHeight - bottomSheetDragAreaHeightPx)
    }

    val isSheetExpanding by derivedStateOf {
        sheetShrinkFactor < 1f
    }

    private val shrinkPlayerHeight = playerShrinkHeightPx + navigationBarHeight
    private val playerExpandHeightAnchors =
        DraggableAnchors {
            PlayerState.Shrink at shrinkPlayerHeight
            PlayerState.Expand at screenHeight.toFloat()
        }

    val playerExpandState =
        AnchoredDraggableState(
            initialValue = PlayerState.Shrink,
            anchors = playerExpandHeightAnchors,
            positionalThreshold = { with(density) { 26.dp.toPx() } },
            velocityThreshold = { with(density) { 20.dp.toPx() } },
            snapAnimationSpec = spring(),
            decayAnimationSpec = exponentialDecay(),
        )

    val playerExpandFactor by derivedStateOf {
        (playerExpandState.offset - shrinkPlayerHeight).div(screenHeight.toFloat() - shrinkPlayerHeight)
    }
    val isPlayerExpanding by derivedStateOf {
        playerExpandFactor > 0f
    }

    val playerState: PlayerState by
    derivedStateOf {
        playerExpandState.currentValue
    }

    fun shrinkPlayerLayout() {
        animaScope.launch {
            playerExpandState.animateTo(PlayerState.Shrink)
        }
    }

    fun expandPlayerLayout() {
        animaScope.launch {
            playerExpandState.animateTo(PlayerState.Expand)
        }
    }
}
