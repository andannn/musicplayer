package com.andanana.musicplayer.core.player.repository

import androidx.media3.common.MediaItem
import kotlinx.coroutines.flow.Flow

sealed interface PlayerState {
    object Idle : PlayerState
    object Buffering : PlayerState
    data class Playing(val currentPositionMs: Long) : PlayerState
    data class Paused(val currentPositionMs: Long) : PlayerState
    object PlayBackEnd : PlayerState
    data class Error(val throwable: Throwable) : PlayerState
}

interface PlayerRepository {
    val currentPositionMs: Long
    fun observePlayerState(): Flow<PlayerState>
    fun observePlayingMediaItem(): Flow<MediaItem?>
    fun setPlayList(mediaItems: List<MediaItem>)
    fun seekToMediaIndex(index: Int)
    fun play()
    fun pause()
    fun next()
    fun seekTo(time: Int)
    fun previous()
}
