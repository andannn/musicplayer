package com.andannn.melodify.feature.player.lyrics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import timber.log.Timber

@Composable
fun rememberSyncedLyricsState(
    syncedLyrics: String
) = remember(syncedLyrics) {
    SyncedLyricsState(syncedLyrics = syncedLyrics)
}

data class SyncedLyricsLine(
    val timeMs: Long,
    val lyrics: String,
)

private const val TAG = "SyncedLyricsState"

class SyncedLyricsState(
    syncedLyrics: String,
) {
    private val _syncedLyricsLines = mutableStateOf(parseSyncedLyrics(syncedLyrics))
    val syncedLyricsLines
        get() = _syncedLyricsLines.value

    var currentPlayingIndex by mutableIntStateOf(-1)

    fun onPositionChanged(currentPositionMs: Long) {
        currentPlayingIndex =
            _syncedLyricsLines.value.indexOfFirst {
                currentPositionMs < it.timeMs
            } - 1
        Timber.tag(TAG)
            .d("onPositionChanged: $currentPositionMs, currentIndex $currentPlayingIndex")
    }
}

fun parseSyncedLyrics(plainLyrics: String): List<SyncedLyricsLine> {
    val regex = Regex("""\[(\d{2}):(\d{2})\.(\d{2})\] (.+)""")
    val matches = regex.findAll(plainLyrics)

    return matches
        .map { match ->
            val (minutesString, secondsString, millisecondsString, lyrics) = match.destructured
            val minutes = minutesString.toIntOrNull() ?: 0
            val seconds = secondsString.toIntOrNull() ?: 0
            val milliSeconds = millisecondsString.toIntOrNull() ?: 0
            val timeMs = minutes.times(60 * 1000)
                .plus(seconds * 1000)
                .plus(milliSeconds)
                .toLong()
            SyncedLyricsLine(timeMs = timeMs, lyrics = lyrics)
        }
        .toList()
}