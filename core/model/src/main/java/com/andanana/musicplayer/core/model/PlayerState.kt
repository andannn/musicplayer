package com.andanana.musicplayer.core.model

sealed interface PlayerState {
    data object Idle : PlayerState

    data object Buffering : PlayerState

    data class Playing(val currentPositionMs: Long) : PlayerState

    data class Paused(val currentPositionMs: Long) : PlayerState

    data object PlayBackEnd : PlayerState

    data class Error(val throwable: Throwable) : PlayerState
}
