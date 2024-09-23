package com.andannn.melodify.core.player

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.andannn.melodify.core.domain.model.PlayerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PlayerWrapper {
    fun setPlayer(player: Player?)

    val currentPositionMs: Long

    val playerState: PlayerState

    val playingIndexInQueue: Int

    fun observePlayerState(): StateFlow<PlayerState>

    fun observePlayListQueue(): StateFlow<List<MediaItem>>

    fun observePlayingMedia(): Flow<MediaItem?>

    fun observeIsShuffle(): StateFlow<Boolean>

    fun observePlayMode(): StateFlow<Int>
}
