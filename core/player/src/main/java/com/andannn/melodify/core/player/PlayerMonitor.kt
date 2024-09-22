package com.andannn.melodify.core.player

import androidx.media3.common.MediaItem
import com.andannn.melodify.core.domain.model.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface PlayerMonitor {
    val currentPositionMs: Long

    val playerState: PlayerState

    val playingIndexInQueue: Int

    fun observePlayerState(): StateFlow<PlayerState>

    fun observePlayListQueue(): StateFlow<List<MediaItem>>

    fun observePlayingMedia(): StateFlow<MediaItem?>

    fun observeIsShuffle(): StateFlow<Boolean>

    fun observePlayMode(): StateFlow<Int>
}
