package com.andannn.melodify.core.player

import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import com.andannn.melodify.core.domain.model.PlayerState
import com.andannn.melodify.core.domain.model.util.CoroutineTicker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "PlayerRepository"

interface PlayerWrapper {
    fun setUpPlayer(player: Player)

    fun release()

    val currentPositionMs: Long

    val playerState: PlayerState

    val playingIndexInQueue: Int

    fun observePlayerState(): StateFlow<PlayerState>

    val playList: List<MediaItem>

    fun observePlayListQueue(): Flow<List<MediaItem>>

    fun observePlayingMedia(): Flow<MediaItem?>

    fun observeIsShuffle(): StateFlow<Boolean>

    fun observePlayMode(): StateFlow<Int>
}

@Singleton
class PlayerWrapperImpl
@Inject
constructor(
) : PlayerWrapper {
    private var _player: Player? = null

    private val _playerStateFlow = MutableStateFlow<PlayerState>(PlayerState.Idle)

    private val _playerModeFlow = MutableStateFlow(Player.REPEAT_MODE_ALL)
    private val _isShuffleFlow = MutableStateFlow(false)

    private val _playingMediaItemStateFlow = MutableSharedFlow<MediaItem?>(1)
    private val _playingIndexInQueueFlow = MutableStateFlow<Int?>(null)

    private val _playListFlow = MutableSharedFlow<List<MediaItem>>(1)

    private val playerProgressUpdater: CoroutineTicker = CoroutineTicker(delayMs = 1000 / 30L) {
        _playerStateFlow.getAndUpdate { old ->
            if (_player == null) {
                return@getAndUpdate old
            }

            when (old) {
                is PlayerState.Error,
                is PlayerState.PlayBackEnd,
                PlayerState.Idle -> old

                is PlayerState.Buffering -> PlayerState.Paused(_player!!.currentPosition)
                is PlayerState.Paused -> PlayerState.Paused(_player!!.currentPosition)
                is PlayerState.Playing -> PlayerState.Playing(_player!!.currentPosition)
            }
        }
    }

    private val playerListener =
        object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                _playerStateFlow.getAndUpdate { old ->
                    val position = old.currentPositionMs

                    when (playbackState) {
                        Player.STATE_IDLE -> PlayerState.Idle
                        Player.STATE_BUFFERING -> PlayerState.Buffering(position)
                        Player.STATE_READY -> {
                            if (_player!!.isPlaying) {
                                PlayerState.Playing(position)
                            } else {
                                PlayerState.Paused(position)
                            }
                        }

                        Player.STATE_ENDED -> PlayerState.PlayBackEnd(position)
                        else -> error("Impossible")
                    }
                }
            }

            override fun onPlayWhenReadyChanged(
                playWhenReady: Boolean,
                reason: Int,
            ) {
                Timber.tag(TAG)
                    .d("onPlayWhenReadyChanged: playWhenReady $playWhenReady reason $reason")
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                Timber.tag(TAG).d("onIsPlayingChanged: $isPlaying")
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
                _playerStateFlow.value = PlayerState.Error(
                    throwable = error,
                    currentPositionMs = _player!!.currentPosition
                )
            }

            override fun onMediaItemTransition(
                mediaItem: MediaItem?,
                reason: Int,
            ) {
                Timber.tag(TAG).d("onMediaItemTransition: $mediaItem")
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
                    _playListFlow.tryEmit(mediaItems.toList())
                }
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                Timber.tag(TAG).d("onShuffleModeEnabledChanged: $shuffleModeEnabled")
                _isShuffleFlow.value = shuffleModeEnabled
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                Timber.tag(TAG).d("onRepeatModeChanged: $repeatMode")
                _playerModeFlow.value = repeatMode
            }
        }

    override fun setUpPlayer(player: Player) {
        Timber.tag(TAG).d("setUpPlayer")
        player.prepare()
        player.addListener(playerListener)
        player.repeatMode = Player.REPEAT_MODE_ALL
        playerProgressUpdater.startTicker()

        _player = player
    }

    override fun release() {
        Timber.tag(TAG).d("release")
        _playerStateFlow.value = PlayerState.Idle
        _playerModeFlow.value = Player.REPEAT_MODE_ALL
        _isShuffleFlow.value = false
        _playingIndexInQueueFlow.value = null
        playerProgressUpdater.stopTicker()

        _player?.release()
        _player = null
    }

    override val currentPositionMs: Long
        get() = _player?.currentPosition ?: 0

    override val playerState: PlayerState
        get() = _playerStateFlow.value

    override val playingIndexInQueue: Int
        get() = _playingIndexInQueueFlow.value!!

    override fun observePlayerState(): StateFlow<PlayerState> = _playerStateFlow
    override val playList: List<MediaItem>
        get()  {
            val timeline = _player?.currentTimeline ?: return emptyList()
            return MutableList(timeline.windowCount) { index ->
                timeline.getWindow(index, Timeline.Window()).mediaItem
            }
        }

    override fun observePlayListQueue() = _playListFlow
        .onStart { playList }

    override fun observePlayingMedia() = _playingMediaItemStateFlow.onStart { emit(null) }

    override fun observeIsShuffle() = _isShuffleFlow

    override fun observePlayMode() = _playerModeFlow
}
