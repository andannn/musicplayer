package com.andanana.musicplayer.feature.player

import android.app.Application
import android.content.ComponentName
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionToken
import com.andanana.musicplayer.core.datastore.repository.SmpPreferenceRepository
import com.andanana.musicplayer.core.data.model.PlayMode
import com.andanana.musicplayer.core.player.PlayerMonitor
import com.andanana.musicplayer.core.player.PlayerState
import com.andanana.musicplayer.core.player.util.CoroutineTicker
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PlayerStateViewModel"

@HiltViewModel
class PlayerStateViewModel @Inject constructor(
    application: Application,
    private val playerMonitor: PlayerMonitor,
    private val smpPreferenceRepository: SmpPreferenceRepository,
//    private val useCases: PlayListUseCases
) : ViewModel() {
    private var browserFuture: ListenableFuture<MediaBrowser> = MediaBrowser.Builder(
        application,
        SessionToken(
            application,
            ComponentName(application, "com.andanana.musicplayer.PlayerService")
        )
    )
        .buildAsync()

    private val browser: MediaBrowser?
        get() = if (browserFuture.isDone && !browserFuture.isCancelled) browserFuture.get() else null

    private val interactingMusicItem: Flow<MediaItem?> = playerMonitor.observePlayingMedia()

    private val playModeFlow = smpPreferenceRepository.userData
        .map { it.playMode }

    private val isCurrentMusicFavorite = flowOf(false)

    private val updateProgressEventFlow = MutableSharedFlow<Unit>()

    private val isBrowserReady = MutableStateFlow(false)

    val playerUiStateFlow =
        combine(
            isBrowserReady,
            interactingMusicItem,
            playerMonitor.observePlayerState(),
            playModeFlow,
            updateProgressEventFlow
        ) { isBrowserReady, interactingMusicItem, state, playMode, _ ->
            if (interactingMusicItem == null || !isBrowserReady) {
                PlayerUiState.Inactive
            } else {
                val duration = browser?.duration?.toFloat() ?: 1f
                PlayerUiState.Active(
                    mediaItem = interactingMusicItem,
                    progress = playerMonitor.currentPositionMs.div(duration),
                    playMode = playMode,
                    isFavorite = false,
                    state = when (state) {
                        is PlayerState.Playing -> {
                            PlayState.PLAYING
                        }

                        is PlayerState.Paused, PlayerState.PlayBackEnd -> {
                            PlayState.PAUSED
                        }

                        else -> PlayState.PAUSED
                    }
                )
            }
        }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), PlayerUiState.Inactive)

    private var coroutineTicker: CoroutineTicker = CoroutineTicker(delayMs = 1000 / 30L) {
        viewModelScope.launch {
            updateProgressEventFlow.emit(Unit)
        }
    }

    init {
        viewModelScope.launch {
            playerMonitor.observePlayerState().collect { state ->
                when (state) {
                    is PlayerState.Playing -> {
                        coroutineTicker.startTicker()
                    }

                    else -> {
                        coroutineTicker.stopTicker()
                    }
                }
            }
        }

        viewModelScope.launch {
            // wait browser build complete.
            browserFuture.await()
            isBrowserReady.value = true
        }
    }

    fun togglePlayState() {
        val state = playerUiStateFlow.value
        if (state is PlayerUiState.Active) {
            playerUiStateFlow.value.let {
                when (state.state) {
                    PlayState.PAUSED -> browser?.play()
                    PlayState.PLAYING -> browser?.pause()
                }
            }
        }
    }

    fun next() {
        browser?.seekToNext()
    }

    fun previous() {
        browser?.seekToPrevious()
    }

    fun onSeekToTime(time: Int) {
        browser?.seekTo(time.toLong())
    }

    fun changePlayMode() {
        viewModelScope.launch {
            val userPreferences = smpPreferenceRepository.userData.first()
            smpPreferenceRepository.setPlayMode(
                playMode = userPreferences.playMode.next()
            )
        }
    }
}

sealed class PlayerUiState {
    object Inactive : PlayerUiState()

    data class Active(
        val state: PlayState = PlayState.PAUSED,
        val progress: Float = 0f,
        val isFavorite: Boolean = false,
        val playMode: PlayMode = PlayMode.REPEAT_ALL,
        val mediaItem: MediaItem
    ) : PlayerUiState()
}

enum class PlayState {
    PAUSED,
    PLAYING
}
