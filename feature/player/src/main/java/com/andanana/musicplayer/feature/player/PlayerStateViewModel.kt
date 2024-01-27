package com.andanana.musicplayer.feature.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaBrowser
import com.andanana.musicplayer.core.data.model.PlayMode
import com.andanana.musicplayer.core.datastore.repository.SmpPreferenceRepository
import com.andanana.musicplayer.core.player.PlayerMonitor
import com.andanana.musicplayer.core.player.PlayerState
import com.andanana.musicplayer.core.player.util.CoroutineTicker
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
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

sealed interface PlayerUiEvent {
    data object OnFavoriteButtonClick : PlayerUiEvent

    data object OnOptionIconClick : PlayerUiEvent

    data object OnPlayButtonClick : PlayerUiEvent

    data object OnNextButtonClick : PlayerUiEvent

    data object OnPlayModeButtonClick : PlayerUiEvent

    data object OnPreviousButtonClick : PlayerUiEvent

    data object OnShuffleButtonClick : PlayerUiEvent

    data class OnProgressChange(val progress: Float) : PlayerUiEvent
}

@HiltViewModel
class PlayerStateViewModel
    @Inject
    constructor(
        private val browserFuture: ListenableFuture<MediaBrowser>,
        private val playerMonitor: PlayerMonitor,
        private val smpPreferenceRepository: SmpPreferenceRepository,
//    private val useCases: PlayListUseCases
    ) : ViewModel() {
        private val browser: MediaBrowser?
            get() = if (browserFuture.isDone && !browserFuture.isCancelled) browserFuture.get() else null

        private val interactingMusicItem = playerMonitor.observePlayingMedia()

        private val playModeFlow =
            smpPreferenceRepository.userData
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
                updateProgressEventFlow,
            ) { isBrowserReady, interactingMusicItem, state, playMode, _ ->
                if (interactingMusicItem == null || !isBrowserReady) {
                    PlayerUiState.Inactive
                } else {
                    val duration = browser?.duration ?: 0L
                    PlayerUiState.Active(
                        mediaItem = interactingMusicItem,
                        duration = duration,
                        progress = playerMonitor.currentPositionMs.toFloat().div(duration),
                        playMode = playMode,
                        isFavorite = false,
                        state =
                            when (state) {
                                is PlayerState.Playing -> {
                                    PlayState.PLAYING
                                }

                                else -> PlayState.PAUSED
                            },
                    )
                }
            }
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), PlayerUiState.Inactive)

        private var coroutineTicker: CoroutineTicker =
            CoroutineTicker(delayMs = 1000 / 30L) {
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

        fun onEvent(event: PlayerUiEvent) {
            when (event) {
                PlayerUiEvent.OnFavoriteButtonClick -> {}
                PlayerUiEvent.OnPlayModeButtonClick -> {}
                PlayerUiEvent.OnPlayButtonClick -> togglePlayState()
                PlayerUiEvent.OnPreviousButtonClick -> previous()
                PlayerUiEvent.OnNextButtonClick -> next()
                PlayerUiEvent.OnShuffleButtonClick -> {}
                PlayerUiEvent.OnOptionIconClick -> Unit
                is PlayerUiEvent.OnProgressChange -> {
                    val time =
                        with((playerUiStateFlow.value as PlayerUiState.Active)) {
                            duration.times(event.progress).toLong()
                        }
                    seekToTime(time)
                }
            }
        }

        private fun togglePlayState() {
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

        private fun previous() {
            browser?.seekToPrevious()
        }

        fun seekToTime(time: Long) {
            browser?.seekTo(time.toLong())
        }

        fun changePlayMode() {
            viewModelScope.launch {
                val userPreferences = smpPreferenceRepository.userData.first()
                smpPreferenceRepository.setPlayMode(
                    playMode = userPreferences.playMode.next(),
                )
            }
        }
    }

sealed class PlayerUiState {
    data object Inactive : PlayerUiState()

    data class Active(
        val state: PlayState = PlayState.PAUSED,
        val duration: Long = 0L,
        val progress: Float = 0f,
        val isFavorite: Boolean = false,
        val playMode: PlayMode = PlayMode.REPEAT_ALL,
        val mediaItem: MediaItem,
    ) : PlayerUiState()
}

enum class PlayState {
    PAUSED,
    PLAYING,
}
