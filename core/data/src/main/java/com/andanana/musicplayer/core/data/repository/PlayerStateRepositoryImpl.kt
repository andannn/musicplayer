package com.andanana.musicplayer.core.data.repository

import com.andanana.musicplayer.core.model.PlayerState
import com.andanana.musicplayer.core.player.PlayerMonitor
import javax.inject.Inject

class PlayerStateRepositoryImpl
    @Inject
    constructor(
        private val playerMonitor: PlayerMonitor,
    ) : PlayerStateRepository {
        override val currentPositionMs: Long
            get() = playerMonitor.currentPositionMs

        override val playerState: PlayerState
            get() = playerMonitor.playerState

        override fun observePlayerState() = playerMonitor.observePlayerState()

        override fun observePlayListQueue() = playerMonitor.observePlayListQueue()

        override fun observePlayingMedia() = playerMonitor.observePlayingMedia()
    }
