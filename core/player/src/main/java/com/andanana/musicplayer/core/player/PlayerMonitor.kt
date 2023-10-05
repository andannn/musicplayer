package com.andanana.musicplayer.core.player

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.andanana.musicplayer.core.data.model.PlayMode
import kotlinx.coroutines.flow.Flow

sealed interface PlayerState {
    object Idle : PlayerState

    object Buffering : PlayerState

    data class Playing(val currentPositionMs: Long) : PlayerState

    data class Paused(val currentPositionMs: Long) : PlayerState

    object PlayBackEnd : PlayerState

    data class Error(val throwable: Throwable) : PlayerState
}

fun PlayMode.toExoPlayerMode() = when (this) {
    PlayMode.REPEAT_ONE -> Player.REPEAT_MODE_ONE
    PlayMode.REPEAT_OFF -> Player.REPEAT_MODE_OFF
    PlayMode.REPEAT_ALL -> Player.REPEAT_MODE_ALL
    PlayMode.SHUFFLE -> -1
}

interface PlayerMonitor {
    val currentPositionMs: Long

    val playerState: PlayerState

    fun observePlayerState(): Flow<PlayerState>

    fun observePlayListQueue(): Flow<List<MediaItem>>

    fun observePlayingMedia(): Flow<MediaItem?>

//    fun setRepeatMode(playMode: PlayMode)
}
