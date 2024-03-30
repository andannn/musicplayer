package com.andanana.musicplayer.core.data.repository

import androidx.media3.common.MediaItem
import com.andanana.musicplayer.core.model.PlayerState
import com.andanana.musicplayer.core.player.PlayerMonitor
import kotlinx.coroutines.flow.StateFlow
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

        override val playingIndexInQueue: Int
            get() = playerMonitor.playingIndexInQueue

        override val playingMediaStateFlow: StateFlow<MediaItem?>
            get() = playerMonitor.observePlayingMedia()

        override val playListQueueStateFlow: StateFlow<List<MediaItem>>
            get() = playerMonitor.observePlayListQueue()

        override fun observeIsShuffle() = playerMonitor.observeIsShuffle()

        override fun observePlayMode() = playerMonitor.observePlayMode()

        override fun observePlayerState() = playerMonitor.observePlayerState()
    }
