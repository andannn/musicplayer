package com.andanana.musicplayer.core.data.repository

import androidx.media3.common.MediaItem
import com.andanana.musicplayer.core.model.PlayerState
import kotlinx.coroutines.flow.Flow

interface PlayerStateRepository {
    val currentPositionMs: Long

    val playerState: PlayerState

    fun observePlayerState(): Flow<PlayerState>

    fun observePlayListQueue(): Flow<List<MediaItem>>

    fun observePlayingMedia(): Flow<MediaItem?>
}
