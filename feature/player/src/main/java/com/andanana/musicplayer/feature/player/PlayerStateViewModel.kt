package com.andanana.musicplayer.feature.player

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.data.data.MediaStoreSource
import com.andanana.musicplayer.core.database.usecases.PlayListUseCases
import com.andanana.musicplayer.core.datastore.repository.SmpPreferenceRepository
import com.andanana.musicplayer.core.model.MusicInfo
import com.andanana.musicplayer.core.model.PlayMode
import com.andanana.musicplayer.core.player.repository.PlayerRepository
import com.andanana.musicplayer.core.player.repository.PlayerState
import com.andanana.musicplayer.core.player.util.CoroutineTicker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PlayerStateViewModel"

@HiltViewModel
class PlayerStateViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val mediaStoreSource: MediaStoreSource,
    private val smpPreferenceRepository: SmpPreferenceRepository,
    private val useCases: PlayListUseCases
) : ViewModel() {

    private val interactingMusicItem: StateFlow<MusicInfo?> = MutableStateFlow(null)
//        playerRepository.observePlayingUri()
//            .map { uri ->
//                uri?.lastPathSegment?.toLong()?.let {
//                    localMusicRepository.getMusicInfoById(it)
//                }
//            }
//            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val playModeFlow = smpPreferenceRepository.userData
        .map { it.playMode }

    private val musicInFavorite = useCases.getMusicInFavorite.invoke()
    private val isCurrentMusicFavorite = combine(
        playerRepository.observePlayingUri(),
        musicInFavorite
    ) { playingUri, favoriteList ->
        favoriteList.map {
            it.musicEntity.id
        }.contains(playingUri?.lastPathSegment?.toLong())
    }
    private val updateProgressEventFlow = MutableSharedFlow<Unit>()
    val playerUiStateFlow =
        combine(
            interactingMusicItem,
            playerRepository.observePlayerState(),
            playModeFlow,
            isCurrentMusicFavorite,
            updateProgressEventFlow
        ) { interactingMusicItem, state, playMode, isCurrentMusicFavorite, _ ->
            if (interactingMusicItem == null) {
                PlayerUiState.Inactive
            } else {
                PlayerUiState.Active(
                    musicInfo = interactingMusicItem,
                    progress = playerRepository.currentPositionMs.div(interactingMusicItem.duration.toFloat()),
                    playMode = playMode,
                    isFavorite = isCurrentMusicFavorite,
                    state = when (state) {
                        is PlayerState.Playing -> {
                            PlayState.PLAYING
                        }
                        is PlayerState.Paused, PlayerState.PlayBackEnd -> {
                            PlayState.PAUSED
                        }
                        is PlayerState.Error, PlayerState.Idle, PlayerState.PlayBackEnd -> {
                            PlayState.LOADING
                        }
                        PlayerState.Buffering -> {
                            PlayState.LOADING
                        }
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
            interactingMusicItem.collect {
                Log.d(TAG, ": playerUiStateFlow state $it")
            }
        }
        viewModelScope.launch {
            playModeFlow.collect { playMode ->
                playerRepository.setRepeatMode(playMode)
            }
        }
        viewModelScope.launch {
            playerRepository.observePlayerState().collect { state ->
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

    fun togglePlayState() {
        val state = playerUiStateFlow.value
        if (state is PlayerUiState.Active) {
            playerUiStateFlow.value.let {
                when (state.state) {
                    PlayState.PAUSED -> playerRepository.play()
                    PlayState.PLAYING -> playerRepository.pause()
                    else -> Unit
                }
            }
        }
    }

    fun next() {
        playerRepository.next()
    }

    fun previous() {
        playerRepository.previous()
    }

    fun onSeekToTime(time: Int) {
        playerRepository.seekTo(time)
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
        val state: PlayState = PlayState.LOADING,
        val progress: Float = 0f,
        val isFavorite: Boolean = false,
        val playMode: PlayMode = PlayMode.REPEAT_ALL,
        val musicInfo: MusicInfo
    ) : PlayerUiState()
}

enum class PlayState {
    PAUSED,
    PLAYING,
    LOADING
}
