package com.andannn.melodify.feature.player.ui.shrinkable.bottom.lyrics

import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun rememberSyncedLyricsState(
    syncedLyrics: String,
    lazyListState: LazyListState = rememberLazyListState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onRequestSeek: (timeMs: Long) -> Unit = { },
): SyncedLyricsState {
    val onRequestSeekState = rememberUpdatedState(onRequestSeek)
    return remember(syncedLyrics, lazyListState) {
        SyncedLyricsState(
            syncedLyrics = syncedLyrics,
            lazyListState = lazyListState,
            coroutineScope = coroutineScope,
            onRequestSeekState = onRequestSeekState
        )
    }
}

data class SyncedLyricsLine(
    val startTimeMs: Long,
    val endTimeMs: Long = 0L,
    val lyrics: String,
)

private const val TAG = "SyncedLyricsState"

sealed interface LyricsState {
    data object AutoScrolling : LyricsState
    data class Seeking(val currentSeekIndex: Int) : LyricsState
    data class WaitingSeekingResult(val requestTimeMs: Long) : LyricsState
}

class SyncedLyricsState(
    syncedLyrics: String,
    val lazyListState: LazyListState,
    private val coroutineScope: CoroutineScope,
    private val onRequestSeekState: State<(timeMs: Long) -> Unit>,
) : CoroutineScope by coroutineScope {
    var syncedLyricsLines by mutableStateOf(parseSyncedLyrics(syncedLyrics))
    var lyricsState by mutableStateOf<LyricsState>(LyricsState.AutoScrolling)
    var currentPlayingIndex by mutableIntStateOf(0)

    private var waitingToCancelSeekJob: Job? = null

    init {
        launch {
            lazyListState.interactionSource.interactions.collect {
                when (it) {
                    is DragInteraction.Start -> {
                        onDragStart()
                    }

                    is DragInteraction.Stop -> {
                        onDragStop()
                    }
                }
            }
        }

        launch {
            snapshotFlow {
                lazyListState.firstVisibleItemIndex
            }.collect { firstVisibleIndex ->
                Timber.tag(TAG).d("First visible item index: $firstVisibleIndex")
                if (lyricsState is LyricsState.Seeking) {
                    lyricsState = LyricsState.Seeking(
                        currentSeekIndex = firstVisibleIndex,
                    )
                }
            }
        }
    }

    private fun onDragStop() {
        Timber.tag(TAG).d("onDragStop")

        waitingToCancelSeekJob = launch {
            delay(5 * 1000)
            lyricsState = LyricsState.AutoScrolling
        }
    }

    private fun onDragStart() {
        Timber.tag(TAG).d("onDragStart")

        waitingToCancelSeekJob?.cancel()
        lyricsState = LyricsState.Seeking(
            lazyListState.firstVisibleItemIndex,
        )
    }

    fun onPositionChanged(currentPositionMs: Long) {
        val state = lyricsState
        if (state is LyricsState.WaitingSeekingResult) {
            if (state.requestTimeMs != currentPositionMs) {
                return
            }

            lyricsState = LyricsState.AutoScrolling
        }

        currentPlayingIndex = syncedLyricsLines
            .indexOfFirst {
                currentPositionMs >= it.startTimeMs && currentPositionMs < it.endTimeMs
            }
            .coerceAtLeast(0)

        Timber.tag(TAG)
            .d("onPositionChanged: $currentPositionMs, currentIndex $currentPlayingIndex")
    }

    fun onSeekTimeClick(time: Long) {
        Timber.tag(TAG).d("onSeekTimeClick: $time")

        lyricsState = LyricsState.WaitingSeekingResult(time)
        onRequestSeekState.value(time)
    }
}

fun parseSyncedLyrics(plainLyrics: String): List<SyncedLyricsLine> {
    val regex = Regex("""\[(\d{2}):(\d{2})\.(\d{2})\] (.*)""")
    val matches = regex.findAll(plainLyrics)

    val listWithoutEndTime = matches
        .map { match ->
            val (minutesString, secondsString, millisecondsString, lyrics) = match.destructured
            val minutes = minutesString.toIntOrNull() ?: 0
            val seconds = secondsString.toIntOrNull() ?: 0
            val milliSeconds = millisecondsString.toIntOrNull() ?: 0
            val timeMs = minutes.times(60 * 1000)
                .plus(seconds * 1000)
                .plus(milliSeconds)
                .toLong()
            SyncedLyricsLine(startTimeMs = timeMs, lyrics = lyrics)
        }
        .toList()

    return listWithoutEndTime.mapIndexed { index, item ->
        val nextStartTime = listWithoutEndTime.getOrNull(index + 1)?.startTimeMs ?: Long.MAX_VALUE
        item.copy(endTimeMs = nextStartTime)
    }
}