package com.andanana.musicplayer.core.player.repository

import kotlinx.coroutines.flow.Flow

sealed interface PlayerState {
    object Idle : PlayerState
    object Buffering : PlayerState
    object Playing : PlayerState
    object Paused : PlayerState
    object PlayBackEnd : PlayerState
    data class Error(val throwable: Throwable) : PlayerState
}

interface PlayerRepository {
    fun observePlayerState(): Flow<PlayerState>
}



