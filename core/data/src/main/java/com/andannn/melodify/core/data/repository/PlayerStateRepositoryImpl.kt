package com.andannn.melodify.core.data.repository

import com.andannn.melodify.core.data.util.fromRepeatMode
import com.andannn.melodify.core.data.util.toAppItem
import com.andannn.melodify.core.domain.model.AudioItemModel
import com.andannn.melodify.core.domain.model.PlayMode
import com.andannn.melodify.core.domain.model.PlayerState
import com.andannn.melodify.core.domain.repository.PlayerStateRepository
import com.andannn.melodify.core.player.PlayerMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override val playListQueue: List<AudioItemModel>
        get() = playerMonitor.observePlayListQueue().value.map {
            it.toAppItem() as? AudioItemModel ?: error("invalid")
        }

    override val playingMediaStateFlow: Flow<AudioItemModel?>
        get() = playerMonitor.observePlayingMedia().map {
            it?.toAppItem() as? AudioItemModel
        }

    override val playListQueueStateFlow: Flow<List<AudioItemModel>>
        get() = playerMonitor.observePlayListQueue().map { items ->
            items.mapNotNull {
                it.toAppItem() as? AudioItemModel
            }
        }

    override fun observeIsShuffle() = playerMonitor.observeIsShuffle()
    override val playMode: PlayMode
        get() = playerMonitor.observePlayMode().value.let {
            fromRepeatMode(it)
        }

    override fun observePlayMode() = playerMonitor.observePlayMode().map {
        fromRepeatMode(it)
    }

    override fun observePlayerState() = playerMonitor.observePlayerState()
}
