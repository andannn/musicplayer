package com.andannn.melodify.core.data.repository

import com.andannn.melodify.core.data.util.fromRepeatMode
import com.andannn.melodify.core.data.util.toAppItem
import com.andannn.melodify.core.domain.model.AudioItemModel
import com.andannn.melodify.core.domain.model.PlayMode
import com.andannn.melodify.core.domain.model.PlayerState
import com.andannn.melodify.core.domain.repository.PlayerStateRepository
import com.andannn.melodify.core.player.PlayerWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayerStateRepositoryImpl
@Inject
constructor(
    private val playerWrapper: PlayerWrapper,
) : PlayerStateRepository {
    override val currentPositionMs: Long
        get() = playerWrapper.currentPositionMs

    override val playerState: PlayerState
        get() = playerWrapper.playerState

    override val playingIndexInQueue: Int
        get() = playerWrapper.playingIndexInQueue

    override val playListQueue: List<AudioItemModel>
        get() = playerWrapper.observePlayListQueue().value.map {
            it.toAppItem() as? AudioItemModel ?: error("invalid")
        }

    override val playingMediaStateFlow: Flow<AudioItemModel?>
        get() = playerWrapper.observePlayingMedia().map {
            it?.toAppItem() as? AudioItemModel
        }

    override val playListQueueStateFlow: Flow<List<AudioItemModel>>
        get() = playerWrapper.observePlayListQueue().map { items ->
            items.mapNotNull {
                it.toAppItem() as? AudioItemModel
            }
        }

    override fun observeIsShuffle() = playerWrapper.observeIsShuffle()
    override val playMode: PlayMode
        get() = playerWrapper.observePlayMode().value.let {
            fromRepeatMode(it)
        }

    override fun observePlayMode() = playerWrapper.observePlayMode().map {
        fromRepeatMode(it)
    }

    override fun observePlayerState() = playerWrapper.observePlayerState()
}
