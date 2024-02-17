package com.andanana.musicplayer.core.player

import androidx.media3.common.MediaItem
import com.andanana.musicplayer.core.model.PlayMode
import com.andanana.musicplayer.core.model.PlayerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PlayerMonitor {
    val currentPositionMs: Long

    val playerState: PlayerState

    fun observePlayerState(): Flow<PlayerState>

    fun observePlayListQueue(): Flow<List<MediaItem>>

    fun observePlayingMedia(): Flow<MediaItem?>

    fun observeIsShuffle(): StateFlow<Boolean>

    fun observePlayMode(): StateFlow<PlayMode>
}
