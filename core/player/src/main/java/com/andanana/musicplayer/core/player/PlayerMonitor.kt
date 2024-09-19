package com.andanana.musicplayer.core.player

import androidx.media3.common.MediaItem
import com.andanana.musicplayer.core.domain.model.PlayMode
import com.andanana.musicplayer.core.domain.model.PlayerState
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
