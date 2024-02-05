package com.andanana.musicplayer.feature.player.widget

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val TAG = "BottomPlayQueueSheet"

enum class BottomSheetState { Shrink, Expand }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomPlayQueueSheet(
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope(),
    sheetMaxHeightDp: Dp,
    sheetMinHeightDp: Dp,
    state: AnchoredDraggableState<BottomSheetState>,
) {
    val density = LocalDensity.current
    val shrinkOffset = state.anchors.positionOf(BottomSheetState.Shrink)
    val expandOffset = state.anchors.positionOf(BottomSheetState.Expand)

    // shrink is 0f, expand is 1f
    val expandFactor by remember {
        derivedStateOf {
            1 - state.offset.div(shrinkOffset - expandOffset)
        }
    }

    val isExpand by remember {
        derivedStateOf {
            state.currentValue == BottomSheetState.Expand
        }
    }

    BackHandler(enabled = isExpand) {
        scope.launch {
            state.animateTo(BottomSheetState.Shrink)
        }
    }

    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .height(sheetMaxHeightDp)
                .offset {
                    IntOffset(
                        0,
                        state
                            .requireOffset()
                            .roundToInt(),
                    )
                },
//                .graphicsLayer {
//                    alpha = expandFactor
//                }
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
    ) {
        Column {
            Box(
                modifier =
                    Modifier
                        .height(BottomSheetDragAreaHeight)
                        .anchoredDraggable(state, orientation = Orientation.Vertical)
                        .fillMaxWidth(),
            ) {
                Text(
                    modifier =
                        Modifier.align(Alignment.Center),
                    text = "Up next",
                )
            }

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color.Blue),
            ) {
            }
        }
    }
}
