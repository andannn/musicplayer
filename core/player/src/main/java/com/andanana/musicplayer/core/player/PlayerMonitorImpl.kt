package com.andanana.musicplayer.core.player

import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import com.andanana.musicplayer.core.data.model.PlayMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "PlayerRepositoryImpl"

@Singleton
class PlayerMonitorImpl @Inject constructor(
    private val player: Player
) : PlayerMonitor {

    private val playerStateFlow = MutableStateFlow<PlayerState>(PlayerState.Idle)

    private val playingMediaItemStateFlow = MutableStateFlow<MediaItem?>(null)

    private val playListFlow = MutableStateFlow<List<MediaItem>>(emptyList())

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            val state = when (playbackState) {
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

        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
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

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            Log.d(
                TAG,
                "onMediaItemTransition: ${mediaItem?.localConfiguration?.uri}  reason $reason"
            )
            playingMediaItemStateFlow.value = mediaItem
        }

        override fun onPositionDiscontinuity(
            oldPosition: Player.PositionInfo,
            newPosition: Player.PositionInfo,
            reason: Int
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

        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            super.onTimelineChanged(timeline, reason)

            MutableList(timeline.windowCount) { index ->
                timeline.getWindow(index, Timeline.Window()).mediaItem
            }.also { mediaItems ->
                playListFlow.value = mediaItems.toList()
            }
        }
    }

    init {
        player.addListener(playerListener)
    }

    override val currentPositionMs: Long
        get() = player.currentPosition

    override val playerState: PlayerState
        get() = playerStateFlow.value

    override fun observePlayerState(): Flow<PlayerState> = playerStateFlow

    override fun observePlayListQueue(): Flow<List<MediaItem>> = playListFlow

    override fun observePlayingMedia(): Flow<MediaItem?> = playingMediaItemStateFlow

//    override fun setRepeatMode(playMode: PlayMode) {
//        Log.d(TAG, "setRepeatMode: $playMode")
//        if (playMode == PlayMode.SHUFFLE) {
//            player.repeatMode = PlayMode.REPEAT_ALL.toExoPlayerMode()
//            player.shuffleModeEnabled = true
//        } else {
//            player.repeatMode = playMode.toExoPlayerMode()
//            player.shuffleModeEnabled = false
//        }
//    }
}