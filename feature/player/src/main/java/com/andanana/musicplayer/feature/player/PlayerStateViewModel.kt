package com.andanana.musicplayer.feature.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaBrowser
import com.andanana.musicplayer.core.data.repository.PlayerStateRepository
import com.andanana.musicplayer.core.data.util.combine
import com.andanana.musicplayer.core.data.util.getOrNull
import com.andanana.musicplayer.core.model.PlayMode
import com.andanana.musicplayer.core.model.PlayerState
import com.andanana.musicplayer.core.model.toExoPlayerMode
import com.andanana.musicplayer.core.model.util.CoroutineTicker
import com.andannn.musicplayer.common.drawer.BottomSheetController
import com.andannn.musicplayer.common.drawer.BottomSheetControllerImpl
import com.andannn.musicplayer.common.drawer.BottomSheetModel
import com.andannn.musicplayer.common.drawer.SheetItem
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface PlayerUiEvent {
    data object OnFavoriteButtonClick : PlayerUiEvent

    data class OnOptionIconClick(
        val mediaItem: MediaItem,
    )  : PlayerUiEvent

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
        private val playerMonitor: PlayerStateRepository,
    ) : ViewModel(), BottomSheetController {
        private val interactingMusicItem = playerMonitor.playingMediaStateFlow

        private val playModeFlow = playerMonitor.observePlayMode()

        private val isShuffleFlow = playerMonitor.observeIsShuffle()

        private val playListQueueFlow = playerMonitor.playListQueueStateFlow

//        private val isCurrentMusicFavorite = flowOf(false)

        private val updateProgressEventFlow = MutableSharedFlow<Unit>()

        private val bottomSheetController: BottomSheetController =
            BottomSheetControllerImpl(viewModelScope, browserFuture, playerMonitor)

        val playerUiStateFlow =
            combine(
                interactingMusicItem,
                playerMonitor.observePlayerState(),
                playModeFlow,
                isShuffleFlow,
                playListQueueFlow,
                updateProgressEventFlow,
            ) { interactingMusicItem, state, playMode, isShuffle, playListQueue, _ ->
                if (interactingMusicItem == null) {
                    PlayerUiState.Inactive
                } else {
                    val duration = browserFuture.getOrNull()?.duration ?: 0L
                    PlayerUiState.Active(
                        mediaItem = interactingMusicItem,
                        duration = duration,
                        progress = playerMonitor.currentPositionMs.toFloat().div(duration),
                        playMode = playMode,
                        isShuffle = isShuffle,
                        isFavorite = false,
                        playListQueue = playListQueue,
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
        }

        fun onEvent(event: PlayerUiEvent) {
            when (event) {
                PlayerUiEvent.OnFavoriteButtonClick -> {}
                PlayerUiEvent.OnPlayModeButtonClick -> {
                    val currentPlayMode = playModeFlow.value
                    browserFuture.getOrNull()?.repeatMode = currentPlayMode.next().toExoPlayerMode()
                }

                PlayerUiEvent.OnPlayButtonClick -> togglePlayState()
                PlayerUiEvent.OnPreviousButtonClick -> previous()
                PlayerUiEvent.OnNextButtonClick -> next()
                PlayerUiEvent.OnShuffleButtonClick -> {
                    browserFuture.getOrNull()?.shuffleModeEnabled = !isShuffleFlow.value
                }

                is PlayerUiEvent.OnOptionIconClick -> {
                    onRequestShowSheet(event.mediaItem)
                }

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
                        PlayState.PAUSED -> browserFuture.getOrNull()?.play()
                        PlayState.PLAYING -> browserFuture.getOrNull()?.pause()
                    }
                }
            }
        }

        override val bottomSheetModel: StateFlow<BottomSheetModel?>
            get() = bottomSheetController.bottomSheetModel

        override fun onRequestShowSheet(mediaItem: MediaItem) = bottomSheetController.onRequestShowSheet(mediaItem)

        override fun onDismissRequest(item: SheetItem?) = bottomSheetController.onDismissRequest(item)

        fun next() {
            browserFuture.getOrNull()?.seekToNext()
        }

        private fun previous() {
            browserFuture.getOrNull()?.seekToPrevious()
        }

        private fun seekToTime(time: Long) {
            browserFuture.getOrNull()?.seekTo(time)
        }
    }

sealed class PlayerUiState {
    data object Inactive : PlayerUiState()

    data class Active(
        val state: PlayState = PlayState.PAUSED,
        val isShuffle: Boolean = false,
        val duration: Long = 0L,
        val progress: Float = 0f,
        val isFavorite: Boolean = false,
        val playMode: PlayMode = PlayMode.REPEAT_ALL,
        val mediaItem: MediaItem,
        val playListQueue: List<MediaItem>,
    ) : PlayerUiState()
}

enum class PlayState {
    PAUSED,
    PLAYING,
}
