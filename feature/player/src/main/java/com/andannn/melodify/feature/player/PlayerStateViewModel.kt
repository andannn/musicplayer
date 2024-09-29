package com.andannn.melodify.feature.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andannn.melodify.core.domain.model.AudioItemModel
import com.andannn.melodify.core.domain.repository.MediaControllerRepository
import com.andannn.melodify.core.domain.repository.PlayerStateRepository
import com.andannn.melodify.core.domain.model.PlayMode
import com.andannn.melodify.core.domain.model.PlayerState
import com.andannn.melodify.common.drawer.BottomSheetController
import com.andannn.melodify.common.drawer.SheetItem
import com.andannn.melodify.core.domain.LyricModel
import com.andannn.melodify.core.domain.repository.LyricRepository
import com.andannn.melodify.core.domain.util.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
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

    data class OnDeleteMediaItem(val index: Int) : PlayerUiEvent

    data class OnItemClickInQueue(val item: AudioItemModel) : PlayerUiEvent
}

private const val TAG = "PlayerStateViewModel"

@HiltViewModel
class PlayerStateViewModel
@Inject
constructor(
    private val mediaControllerRepository: MediaControllerRepository,
    private val lyricRepository: LyricRepository,
    private val playerStateRepository: PlayerStateRepository,
    private val bottomSheetController: BottomSheetController
) : ViewModel() {
    private val interactingMusicItem = playerStateRepository.playingMediaStateFlow
    private val playerStateFlow = playerStateRepository
        .observePlayerState()
        .distinctUntilChanged()
        .onEach {
            Timber.tag(TAG).d("play state changed $it")
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val lyricFlow = interactingMusicItem
        .filterNotNull()
        .flatMapLatest {
            lyricRepository.getLyricByMediaStoreIdFlow(it.id)
        }

    private val playModeFlow = playerStateRepository.observePlayMode()

    private val isShuffleFlow = playerStateRepository.observeIsShuffle()

    private val playListQueueFlow = playerStateRepository.playListQueueStateFlow

    val playerUiStateFlow =
        combine(
            interactingMusicItem,
            playerStateFlow,
            playModeFlow,
            isShuffleFlow,
            playListQueueFlow,
            lyricFlow,
        ) { interactingMusicItem, state, playMode, isShuffle, playListQueue, lyric ->
            if (interactingMusicItem == null) {
                PlayerUiState.Inactive
            } else {
                PlayerUiState.Active(
                    state = state,
                    lyric = lyric,
                    mediaItem = interactingMusicItem,
                    duration = mediaControllerRepository.duration ?: 0L,
                    playMode = playMode,
                    isShuffle = isShuffle,
                    isFavorite = false,
                    playListQueue = playListQueue,
                )
            }
        }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), PlayerUiState.Inactive)

    init {
        viewModelScope.launch {
            interactingMusicItem
                .filterNotNull()
                .distinctUntilChanged()
                .onEach { audio ->
                    lyricRepository.tryGetLyricOrIgnore(
                        mediaStoreId = audio.id,
                        trackName = audio.name,
                        artistName = audio.artist,
                        albumName = audio.album,
                    )
                }
                .collect {}
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

            is PlayerUiEvent.OnItemClickInQueue -> {
                val state = playerUiStateFlow.value as PlayerUiState.Active
                mediaControllerRepository.seekMediaItem(
                    mediaItemIndex = state.playListQueue.indexOf(event.item)
                )
            }

            is PlayerUiEvent.OnDeleteMediaItem -> {
                mediaControllerRepository.removeMediaItem(event.index)
            }
        }
    }

    private fun togglePlayState() {
        val state = playerUiStateFlow.value
        if (state is PlayerUiState.Active) {
            playerUiStateFlow.value.let {
                if (state.isPlaying) {
                    mediaControllerRepository.pause()
                } else {
                    mediaControllerRepository.play()
                }
            }
        }
    }

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
        val state: PlayerState = PlayerState.Idle,
        val lyric: LyricModel? = null,
        val isShuffle: Boolean = false,
        val duration: Long = 0L,
        val isFavorite: Boolean = false,
        val playMode: PlayMode = PlayMode.REPEAT_ALL,
        val mediaItem: AudioItemModel,
        val playListQueue: List<AudioItemModel>,
    ) : PlayerUiState() {
        val progress: Float
            get() = state.currentPositionMs.toFloat().div(duration)

        val isPlaying: Boolean
            get() = state is PlayerState.Playing
    }
}
