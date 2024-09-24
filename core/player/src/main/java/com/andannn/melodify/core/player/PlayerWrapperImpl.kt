package com.andannn.melodify.core.player

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import com.andannn.melodify.core.domain.model.PlayerState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "PlayerRepositoryImpl"

@Singleton
class PlayerWrapperImpl
@Inject
constructor(
) : PlayerWrapper {
    private var _player: Player? = null
        set(playerOrNull) {
            if (playerOrNull != null) {
                setUpPlayer(playerOrNull)
            } else {
                release()
            }
            field = playerOrNull
        }

    private val _playerStateFlow = MutableStateFlow<PlayerState>(PlayerState.Idle)

    private val _playerModeFlow = MutableStateFlow(Player.REPEAT_MODE_ALL)
    private val _isShuffleFlow = MutableStateFlow(false)

    private val _playingMediaItemStateFlow = MutableSharedFlow<MediaItem?>(1)
    private val _playingIndexInQueueFlow = MutableStateFlow<Int?>(null)

    private val _playListFlow = MutableStateFlow<List<MediaItem>>(emptyList())

    private val playerListener =
        object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                val state =
                    when (playbackState) {
                        Player.STATE_IDLE -> PlayerState.Idle
                        Player.STATE_BUFFERING -> PlayerState.Buffering
                        Player.STATE_READY -> {
                            with(_player!!) {
                                if (isPlaying) {
                                    PlayerState.Playing(currentPosition)
                                } else {
                                    PlayerState.Paused(currentPosition)
                                }
                            }
                        }

                        Player.STATE_ENDED -> PlayerState.PlayBackEnd
                        else -> error("Impossible")
                    }

                _playerStateFlow.update { state }
            }

            override fun onPlayWhenReadyChanged(
                playWhenReady: Boolean,
                reason: Int,
            ) {
                Log.d(TAG, "onPlayWhenReadyChanged: $playWhenReady  reason $reason")
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                with(_player!!) {
                    if (isPlaying) {
                        _playerStateFlow.value = PlayerState.Playing(currentPosition)
                    } else {
                        val suppressed =
                            playbackSuppressionReason != Player.PLAYBACK_SUPPRESSION_REASON_NONE
                        val playerError = playerError != null
                        if (playbackState == Player.STATE_READY && !suppressed && !playerError) {
                            _playerStateFlow.value = PlayerState.Paused(currentPosition)
                        }
                    }
                }
            }

            override fun onPlayerError(error: PlaybackException) {
// TODO: Define exception type and send back.
                _playerStateFlow.value = PlayerState.Error(error)
            }

            override fun onMediaItemTransition(
                mediaItem: MediaItem?,
                reason: Int,
            ) {
                Log.d(TAG, "onMediaItemTransition: $mediaItem")
                _playingIndexInQueueFlow.value = _player!!.currentMediaItemIndex
                _playingMediaItemStateFlow.tryEmit(mediaItem)
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int,
            ) {
                _playerStateFlow.getAndUpdate { state ->
                    when (state) {
                        is PlayerState.Playing -> {
                            state.copy(currentPositionMs = newPosition.contentPositionMs)
                        }

                        is PlayerState.Paused -> {
                            state.copy(currentPositionMs = newPosition.contentPositionMs)
                        }

                        else -> state
                    }
                }
            }

            override fun onTimelineChanged(
                timeline: Timeline,
                reason: Int,
            ) {
                super.onTimelineChanged(timeline, reason)

                MutableList(timeline.windowCount) { index ->
                    timeline.getWindow(index, Timeline.Window()).mediaItem
                }.also { mediaItems ->
                    _playListFlow.value = mediaItems.toList()
                }
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                Log.d(TAG, "onShuffleModeEnabledChanged: $shuffleModeEnabled")
                _isShuffleFlow.value = shuffleModeEnabled
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                Log.d(TAG, "onRepeatModeChanged: $repeatMode")
                _playerModeFlow.value = repeatMode
            }
        }

    private fun setUpPlayer(player: Player) {
        player.prepare()
        player.addListener(playerListener)
        player.repeatMode = Player.REPEAT_MODE_ALL
    }

    private fun release() {
        _playerStateFlow.value = PlayerState.Idle
        _playerModeFlow.value = Player.REPEAT_MODE_ALL
        _isShuffleFlow.value = false
        _playingIndexInQueueFlow.value = null
        _playListFlow.value = emptyList()

        _player?.release()
        _player = null
    }

    override fun setPlayer(player: Player?) {
        this._player = player
    }

    override val currentPositionMs: Long
        get() = _player?.currentPosition ?: 0

    override val playerState: PlayerState
        get() = _playerStateFlow.value

    override val playingIndexInQueue: Int
        get() = _playingIndexInQueueFlow.value!!

    override fun observePlayerState(): StateFlow<PlayerState> = _playerStateFlow

    override fun observePlayListQueue() = _playListFlow

    override fun observePlayingMedia() = _playingMediaItemStateFlow.onStart { emit(null) }

    override fun observeIsShuffle() = _isShuffleFlow

    override fun observePlayMode() = _playerModeFlow
}
