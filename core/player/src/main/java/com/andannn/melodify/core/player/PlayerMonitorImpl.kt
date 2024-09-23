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
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "PlayerRepositoryImpl"

@Singleton
class PlayerMonitorImpl
@Inject
constructor(
    private val player: Player,
) : PlayerMonitor {
    private val playerStateFlow = MutableStateFlow<PlayerState>(PlayerState.Idle)

    private val playerModeFlow = MutableStateFlow(Player.REPEAT_MODE_ALL)
    private val isShuffleFlow = MutableStateFlow(false)

    private val playingMediaItemStateFlow = MutableSharedFlow<MediaItem?>(1)
    private val playingIndexInQueueFlow = MutableStateFlow<Int?>(null)

    private val playListFlow = MutableStateFlow<List<MediaItem>>(emptyList())

    private val playerListener =
        object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                val state =
                    when (playbackState) {
                        Player.STATE_IDLE -> PlayerState.Idle
                        Player.STATE_BUFFERING -> PlayerState.Buffering
                        Player.STATE_READY -> {
                            if (player.isPlaying) {
                                PlayerState.Playing(player.currentPosition)
                            } else {
                                PlayerState.Paused(player.currentPosition)
                            }
                        }

                        Player.STATE_ENDED -> PlayerState.PlayBackEnd
                        else -> error("Impossible")
                    }

                playerStateFlow.update { state }
            }

            override fun onPlayWhenReadyChanged(
                playWhenReady: Boolean,
                reason: Int,
            ) {
                Log.d(TAG, "onPlayWhenReadyChanged: $playWhenReady  reason $reason")
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    playerStateFlow.value = PlayerState.Playing(player.currentPosition)
                } else {
                    val suppressed =
                        player.playbackSuppressionReason != Player.PLAYBACK_SUPPRESSION_REASON_NONE
                    val playerError = player.playerError != null
                    if (player.playbackState == Player.STATE_READY && !suppressed && !playerError) {
                        playerStateFlow.value = PlayerState.Paused(player.currentPosition)
                    }
                }
            }

            override fun onPlayerError(error: PlaybackException) {
// TODO: Define exception type and send back.
                playerStateFlow.value = PlayerState.Error(error)
            }

            override fun onMediaItemTransition(
                mediaItem: MediaItem?,
                reason: Int,
            ) {
                Log.d(TAG, "onMediaItemTransition: $mediaItem")
                playingIndexInQueueFlow.value = player.currentMediaItemIndex
                playingMediaItemStateFlow.tryEmit(mediaItem)
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int,
            ) {
                playerStateFlow.getAndUpdate { state ->
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
                    playListFlow.value = mediaItems.toList()
                }
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                Log.d(TAG, "onShuffleModeEnabledChanged: $shuffleModeEnabled")
                isShuffleFlow.value = shuffleModeEnabled
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                Log.d(TAG, "onRepeatModeChanged: $repeatMode")
                playerModeFlow.value = repeatMode
            }
        }

    init {
        player.addListener(playerListener)
        player.repeatMode = Player.REPEAT_MODE_ALL
    }

    override val currentPositionMs: Long
        get() = player.currentPosition

    override val playerState: PlayerState
        get() = playerStateFlow.value

    override val playingIndexInQueue: Int
        get() = playingIndexInQueueFlow.value!!

    override fun observePlayerState(): StateFlow<PlayerState> = playerStateFlow

    override fun observePlayListQueue() = playListFlow

    override fun observePlayingMedia() = playingMediaItemStateFlow

    override fun observeIsShuffle() = isShuffleFlow

    override fun observePlayMode() = playerModeFlow
}
