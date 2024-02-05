package com.andanana.musicplayer.feature.player.widget

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
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
    state: AnchoredDraggableState<BottomSheetState>,
) {
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

    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .height(sheetMaxHeightDp)
                .offset {
                    IntOffset(0, state.requireOffset().roundToInt())
                },
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
    ) {
        Box {
            Column(
                modifier =
                    Modifier
                        .graphicsLayer { alpha = expandFactor }
                        .background(MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Box(
                    modifier = Modifier.height(BottomSheetDragAreaHeight),
                )

                Divider()

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                ) {
                    LazyColumn {
                        items(
                            items = listOf(1, 2, 3, 4, 5, 6),
                            key = { it },
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth().height(100.dp),
                                text = it.toString(),
                            )
                        }
                    }
                }
            }

            Box(
                modifier =
                    Modifier
                        .align(Alignment.TopCenter)
                        .height(BottomSheetDragAreaHeight)
                        .anchoredDraggable(state, orientation = Orientation.Vertical)
                        .fillMaxWidth(),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "UP NEXT",
                )
            }
        }
    }
}

@ExperimentalFoundationApi
@Preview
@Composable
fun BottomPlayQueueSheetPreview() {
    val density = LocalDensity.current
    val anchors =
        with(LocalDensity.current) {
            DraggableAnchors {
                BottomSheetState.Shrink at 120.dp.toPx()
                BottomSheetState.Expand at 0f
            }
        }

    val state =
        remember {
            AnchoredDraggableState(
                initialValue = BottomSheetState.Shrink,
                anchors = anchors,
                positionalThreshold = { with(density) { 26.dp.toPx() } },
                velocityThreshold = { with(density) { 20.dp.toPx() } },
                animationSpec = spring(),
            )
        }

    BottomPlayQueueSheet(
        sheetMaxHeightDp = 360.dp,
        state = state,
    )
}
