package com.andanana.melodify.core.domain.repository

import com.andanana.melodify.core.domain.model.AudioItemModel
import com.andanana.melodify.core.domain.model.PlayMode
import com.andanana.melodify.core.domain.model.PlayerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PlayerStateRepository {
    val currentPositionMs: Long

    val playerState: PlayerState

    val playingIndexInQueue: Int

    val playListQueue: List<AudioItemModel>

    val playingMediaStateFlow: Flow<AudioItemModel?>

    val playListQueueStateFlow: Flow<List<AudioItemModel>>

    fun observeIsShuffle(): StateFlow<Boolean>

    val playMode: PlayMode

    fun observePlayMode(): Flow<PlayMode>

    fun observePlayerState(): Flow<PlayerState>
}
