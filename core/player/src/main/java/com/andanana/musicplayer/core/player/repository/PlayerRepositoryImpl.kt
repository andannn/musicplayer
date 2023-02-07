package com.andanana.musicplayer.core.player.repository

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PlayerRepositoryImpl"

class PlayerRepositoryImpl @Inject constructor(
    private val player: Player
) : PlayerRepository {

    private val playerStateFlow = MutableStateFlow<PlayerState>(PlayerState.Idle)

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            val state = when (playbackState) {
                Player.STATE_IDLE -> PlayerState.Idle
                Player.STATE_BUFFERING -> PlayerState.Buffering
                Player.STATE_READY -> {
                    if (player.isPlaying) PlayerState.Playing else PlayerState.Paused
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
                playerStateFlow.value = PlayerState.Playing
            } else {
                val suppressed =
                    player.playbackSuppressionReason != Player.PLAYBACK_SUPPRESSION_REASON_NONE
                val playerError = player.playerError != null
                if (player.playbackState == Player.STATE_READY && !suppressed && !playerError) {
                    playerStateFlow.value = PlayerState.Paused
                }
            }
        }

        override fun onPlaybackSuppressionReasonChanged(playbackSuppressionReason: Int) {
            Log.d(TAG, "onPlaybackSuppressionReasonChanged: $playbackSuppressionReason")
        }

        override fun onPlayerError(error: PlaybackException) {
// TODO: Define exception type and send back.
            playerStateFlow.value = PlayerState.Error(error)
        }
    }

    init {
        val mediaItem: MediaItem =
            MediaItem.fromUri("content://media/external/audio/media/1000008204")
        player.setMediaItem(mediaItem)
        player.addListener(playerListener)
        player.prepare()
        player.play()

        GlobalScope.launch(Dispatchers.Main) {
            player.play()
            delay(1000)
            player.pause()
            delay(1000)
            player.playWhenReady = true
        }
    }

    override fun observePlayerState(): Flow<PlayerState> = playerStateFlow
}
