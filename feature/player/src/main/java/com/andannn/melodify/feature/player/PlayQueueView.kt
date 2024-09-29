package com.andannn.melodify.feature.player

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.andannn.melodify.core.domain.model.AudioItemModel
import com.andannn.melodify.core.designsystem.component.AudioItemView
import com.andannn.melodify.core.designsystem.theme.MelodifyTheme
import com.andannn.melodify.feature.player.widget.BottomSheetState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableItem
import timber.log.Timber
import kotlin.math.roundToInt

private const val TAG = "PlayQueueView"

private enum class SheetTab {
    NEXT_SONG,
    LYRICS,
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayQueueView(
    sheetMaxHeightDp: Dp,
    state: AnchoredDraggableState<BottomSheetState>,
    playListQueue: ImmutableList<AudioItemModel>,
    activeMediaItem: AudioItemModel,
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope(),
    onEvent: (PlayerUiEvent) -> Unit = {},
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
            .graphicsLayer { alpha = expandFactor }
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
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
    ) {
        
        var selectedTab by remember {
            mutableStateOf(SheetTab.NEXT_SONG)
        }
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            ScrollableTabRow(
                modifier = Modifier
                    .anchoredDraggable(state, orientation = Orientation.Vertical)
                    .fillMaxWidth(),
                selectedTabIndex = SheetTab.entries.indexOf(selectedTab),
                containerColor = Color.Transparent
            ) {
                SheetTab.entries.toTypedArray().forEach {
                    Tab(
                        selected = it == selectedTab,
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                        text = @Composable {
                            Text(
                                text = it.toString(),
                            )
                        },
                        onClick = {
                            selectedTab = it
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(3.dp))

            when (selectedTab) {
                SheetTab.NEXT_SONG -> {
                    PlayQueue(
                        modifier = Modifier.weight(1f),
                        onSwapFinished = { from, to ->
                            Timber.tag(TAG).d("PlayQueueView: drag stopped from $from to $to")
                            onEvent(PlayerUiEvent.OnSwapPlayQueue(from, to))
                        },
                        onDeleteFinished = {
                            Timber.tag(TAG).d("onDeleteFinished $it")
                            onEvent(PlayerUiEvent.OnDeleteMediaItem(it))
                        },
                        onItemClick = {
                            onEvent(PlayerUiEvent.OnItemClickInQueue(it))
                        },
                        activeMediaItem = activeMediaItem,
                        playListQueue = playListQueue,
                    )
                }
                SheetTab.LYRICS -> {

                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlayQueue(
    onItemClick: (AudioItemModel) -> Unit,
    onSwapFinished: (from: Int, to: Int) -> Unit,
    onDeleteFinished: (Int) -> Unit,
    playListQueue: ImmutableList<AudioItemModel>,
    activeMediaItem: AudioItemModel,
    modifier: Modifier = Modifier
) {
    val playQueueState = rememberPlayQueueState(
        onSwapFinished = { from, to ->
            Timber.tag(TAG).d("PlayQueueView: drag stopped from $from to $to")
            onSwapFinished(from, to)
        },
        onDeleteFinished = {
            Timber.tag(TAG).d("onDeleteFinished $it")
            onDeleteFinished(it)
        }
    )

    LaunchedEffect(playListQueue) {
        playQueueState.onApplyNewList(playListQueue)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        state = playQueueState.lazyListState
    ) {
        items(
            items = playQueueState.audioItemList,
            key = { it.hashCode() },
        ) { item ->
            ReorderableItem(
                state = playQueueState.reorderableLazyListState,
                key = item.hashCode()
            ) { _ ->
                QueueItem(
                    item = item,
                    isActive = item.extraUniqueId == activeMediaItem.extraUniqueId,
                    onClick = {
                        onItemClick(item)
                    },
                    onSwapFinish = {
                        playQueueState.onStopDrag()
                    },
                    onDismissFinish = {
                        playQueueState.onDismissItem(item)
                    }
                )
            }
        }
    }
}

@Composable
private fun ReorderableCollectionItemScope.QueueItem(
    item: AudioItemModel,
    isActive: Boolean,
    modifier: Modifier = Modifier,
    onSwapFinish: () -> Unit = {},
    onClick: () -> Unit = {},
    onDismissFinish: () -> Unit = {}
) {
    val dismissState = rememberSwipeToDismissBoxState()
    var dismissed by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(dismissState.currentValue) {
        if (dismissed) return@LaunchedEffect

        val state = dismissState.currentValue
        if (state == SwipeToDismissBoxValue.EndToStart || state == SwipeToDismissBoxValue.StartToEnd) {
            onDismissFinish()
            dismissed = true
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Spacer(modifier = Modifier)
        }
    ) {
        AudioItemView(
            modifier = modifier,
            swapIconModifier = Modifier.draggableHandle(
                onDragStopped = onSwapFinish
            ),
            isActive = isActive,
            defaultColor = MaterialTheme.colorScheme.surfaceVariant,
            albumArtUri = item.artWorkUri,
            title = item.name,
            showTrackNum = false,
            artist = item.artist,
            trackNum = item.cdTrackNumber,
            onMusicItemClick = onClick,
        )
    }
}

@ExperimentalFoundationApi
@Preview
@Composable
private fun BottomPlayQueueSheetPreview() {
    MelodifyTheme {
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
                    initialValue = BottomSheetState.Expand,
                    anchors = anchors,
                    positionalThreshold = { with(density) { 26.dp.toPx() } },
                    velocityThreshold = { with(density) { 20.dp.toPx() } },
                    snapAnimationSpec = spring(),
                    decayAnimationSpec = exponentialDecay(),
                )
            }

        PlayQueueView(
            sheetMaxHeightDp = 360.dp,
            state = state,
            playListQueue = listOf(
                AudioItemModel(
                    id = 0,
                    name = "Song 1",
                    modifiedDate = 0,
                    album = "Album 1",
                    albumId = 0,
                    artist = "Artist 1",
                    artistId = 0,
                    cdTrackNumber = 1,
                    discNumberIndex = 0,
                    artWorkUri = "",
                )
            ).toImmutableList(),
            activeMediaItem = AudioItemModel.DEFAULT,
        )
    }
}
