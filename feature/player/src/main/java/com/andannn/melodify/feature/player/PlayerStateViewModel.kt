package com.andannn.melodify.feature.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andannn.melodify.core.domain.model.AudioItemModel
import com.andannn.melodify.core.domain.repository.MediaControllerRepository
import com.andannn.melodify.core.domain.repository.PlayerStateRepository
import com.andannn.melodify.core.domain.model.PlayMode
import com.andannn.melodify.core.domain.model.PlayerState
import com.andannn.melodify.core.domain.model.util.CoroutineTicker
import com.andannn.melodify.core.domain.util.combine
import com.andannn.melodify.common.drawer.BottomSheetController
import com.andannn.melodify.common.drawer.BottomSheetModel
import com.andannn.melodify.common.drawer.SheetItem
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
        val mediaItem: AudioItemModel,
    ) : PlayerUiEvent

    data object OnPlayButtonClick : PlayerUiEvent

    data object OnNextButtonClick : PlayerUiEvent

    data object OnPlayModeButtonClick : PlayerUiEvent

    data object OnPreviousButtonClick : PlayerUiEvent

    data object OnShuffleButtonClick : PlayerUiEvent

    data class OnDismissDrawerRequest(val item: SheetItem?) : PlayerUiEvent

    data class OnProgressChange(val progress: Float) : PlayerUiEvent

    data class OnSwapPlayQueue(val from: Int, val to: Int) : PlayerUiEvent
}

@HiltViewModel
class PlayerStateViewModel
@Inject
constructor(
    private val mediaControllerRepository: MediaControllerRepository,
    private val playerStateRepository: PlayerStateRepository,
    private val bottomSheetController: BottomSheetController
) : ViewModel() {
    private val interactingMusicItem = playerStateRepository.playingMediaStateFlow

    private val playModeFlow = playerStateRepository.observePlayMode()

    private val isShuffleFlow = playerStateRepository.observeIsShuffle()

    private val playListQueueFlow = playerStateRepository.playListQueueStateFlow

//        private val isCurrentMusicFavorite = flowOf(false)

    private val updateProgressEventFlow = MutableSharedFlow<Unit>()

    val playerUiStateFlow =
        combine(
            interactingMusicItem,
            playerStateRepository.observePlayerState(),
            playModeFlow,
            isShuffleFlow,
            playListQueueFlow,
            updateProgressEventFlow,
        ) { interactingMusicItem, state, playMode, isShuffle, playListQueue, _ ->
            if (interactingMusicItem == null) {
                PlayerUiState.Inactive
            } else {
                val duration = mediaControllerRepository.duration ?: 0L
                PlayerUiState.Active(
                    mediaItem = interactingMusicItem,
                    duration = duration,
                    progress = playerStateRepository.currentPositionMs.toFloat().div(duration),
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
            playerStateRepository.observePlayerState().collect { state ->
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
                val nextPlayMode = playerStateRepository.playMode.next()
                mediaControllerRepository.setPlayMode(nextPlayMode)
            }

            PlayerUiEvent.OnPlayButtonClick -> togglePlayState()
            PlayerUiEvent.OnPreviousButtonClick -> previous()
            PlayerUiEvent.OnNextButtonClick -> next()
            PlayerUiEvent.OnShuffleButtonClick -> {
                mediaControllerRepository.setShuffleModeEnabled(!isShuffleFlow.value)
            }

            is PlayerUiEvent.OnOptionIconClick -> {
                bottomSheetController.onRequestShowSheet(event.mediaItem)
            }

            is PlayerUiEvent.OnProgressChange -> {
                val time =
                    with((playerUiStateFlow.value as PlayerUiState.Active)) {
                        duration.times(event.progress).toLong()
                    }
                seekToTime(time)
            }

            is PlayerUiEvent.OnDismissDrawerRequest -> {
                with(bottomSheetController) {
                    viewModelScope.onDismissRequest(event.item)
                }
            }

            is PlayerUiEvent.OnSwapPlayQueue -> {
                mediaControllerRepository.moveMediaItem(event.from, event.to)
            }
        }
    }

    private fun togglePlayState() {
        val state = playerUiStateFlow.value
        if (state is PlayerUiState.Active) {
            playerUiStateFlow.value.let {
                when (state.state) {
                    PlayState.PAUSED -> mediaControllerRepository.play()
                    PlayState.PLAYING -> mediaControllerRepository.pause()
                }
            }
        }
    }

    val bottomSheetModel: StateFlow<BottomSheetModel?>
        get() = bottomSheetController.bottomSheetModel

    fun next() {
        mediaControllerRepository.seekToNext()
    }

    private fun previous() {
        mediaControllerRepository.seekToPrevious()
    }

    private fun seekToTime(time: Long) {
        mediaControllerRepository.seekToTime(time)
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
        val mediaItem: AudioItemModel,
        val playListQueue: List<AudioItemModel>,
    ) : PlayerUiState()
}

enum class PlayState {
    PAUSED,
    PLAYING,
}
