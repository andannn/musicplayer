package com.andannn.melodify.feature.player.ui.shrinkable.bottom.queue

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.andannn.melodify.core.domain.model.AudioItemModel
import kotlinx.collections.immutable.ImmutableList
import sh.calvin.reorderable.ReorderableLazyListState
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun rememberPlayQueueState(
    lazyListState: LazyListState = rememberLazyListState(),
    onSwapFinished: (from: Int, to: Int) -> Unit = { _, _ -> },
    onDeleteFinished: (index: Int) -> Unit = { },
): PlayQueueStateHolder {
    val onSwapFinishedState = rememberUpdatedState(onSwapFinished)
    val onDeleteFinishedState = rememberUpdatedState(onDeleteFinished)
    val playQueueState = remember {
        PlayListStateImpl(
            onSwapFinishedState = onSwapFinishedState,
            onDeleteFinishedState = onDeleteFinishedState,
        )
    }

    val reorderableLazyListState =
        rememberReorderableLazyListState(lazyListState) { from, to ->
            playQueueState.onSwapItem(from.index, to.index)
        }

    return remember {
        PlayQueueStateHolder(
            lazyListState = lazyListState,
            playListState = playQueueState,
            reorderableLazyListState = reorderableLazyListState
        )
    }
}

class PlayQueueStateHolder(
    val lazyListState: LazyListState,
    private val playListState: PlayListStateImpl,
    val reorderableLazyListState: ReorderableLazyListState,
) : PlayListState by playListState

interface PlayListState {
    val audioItemList: SnapshotStateList<AudioItemModel>
    fun onStopDrag()
    fun onSwapItem(from: Int, to: Int)
    fun onApplyNewList(list: ImmutableList<AudioItemModel>)
    fun onDismissItem(item: AudioItemModel)
}

class PlayListStateImpl(
    private val onSwapFinishedState: State<(from: Int, to: Int) -> Unit>,
    private val onDeleteFinishedState: State<(index: Int) -> Unit>,
) : PlayListState {
    override val audioItemList = mutableStateListOf<AudioItemModel>()

    private var fromDragIndex: Int? = null
    private var toDragIndex: Int? = null

    override fun onStopDrag() {
        val to = toDragIndex
        val from = fromDragIndex

        if (to != null && from != null) {
            onSwapFinishedState.value(from, to)
        }

        fromDragIndex = null
        toDragIndex = null
    }

    override fun onSwapItem(from: Int, to: Int) {
        toDragIndex = to
        if (fromDragIndex == null) {
            fromDragIndex = from
        }

        with(audioItemList) {
            add(to, removeAt(from))
        }
    }

    override fun onApplyNewList(list: ImmutableList<AudioItemModel>) {
        with(audioItemList) {
            clear()
            addAll(list)
        }
    }

    override fun onDismissItem(item: AudioItemModel) {
        with(audioItemList) {
            val index = indexOf(item)
            removeAt(index)
            onDeleteFinishedState.value(index)
        }
    }
}