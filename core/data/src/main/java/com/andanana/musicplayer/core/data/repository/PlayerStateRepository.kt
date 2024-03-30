package com.andanana.musicplayer.core.data.repository

import androidx.media3.common.MediaItem
import com.andanana.musicplayer.core.model.PlayMode
import com.andanana.musicplayer.core.model.PlayerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PlayerStateRepository {
    val currentPositionMs: Long

    val playerState: PlayerState

    val playingIndexInQueue: Int

    val playingMediaStateFlow: StateFlow<MediaItem?>

    val playListQueueStateFlow: StateFlow<List<MediaItem>>

    fun observeIsShuffle(): StateFlow<Boolean>

    fun observePlayMode(): StateFlow<PlayMode>

    fun observePlayerState(): Flow<PlayerState>
}
