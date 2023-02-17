package com.andanana.musicplayer.core.player

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.database.usecases.PlayListUseCases
import com.andanana.musicplayer.core.model.MusicInfo
import com.andanana.musicplayer.core.player.repository.PlayerRepository
import com.andanana.musicplayer.core.player.repository.PlayerState
import com.andanana.musicplayer.core.player.util.CoroutineTicker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PlayerStateViewModel"

@HiltViewModel
class PlayerStateViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val playerRepository: PlayerRepository,
    private val useCases: PlayListUseCases
) : ViewModel() {

    private val playListFlow =
        savedStateHandle.getStateFlow<List<MusicInfo>>(PLAY_LIST_KEY, emptyList())

    val interactingMusicItem: StateFlow<MusicInfo?> =
        combine(
            playerRepository.observePlayingMediaItem(),
            playListFlow
        ) { playingMediaItem, playList ->
            playList.find {
                it.mediaItem == playingMediaItem
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _playerUiStateFlow = MutableStateFlow<PlayerUiState>(PlayerUiState.Inactive)
    val playerUiStateFlow = _playerUiStateFlow.asStateFlow()

    private val musicInFavorite = useCases.getMusicInFavorite.invoke()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private var coroutineTicker: CoroutineTicker = CoroutineTicker(delayMs = 1000 / 10L) {
        (_playerUiStateFlow.value as? PlayerUiState.Active)?.let { playerState ->
            _playerUiStateFlow.update {
                playerState.copy(
                    progress = playerRepository.currentPositionMs.div(playerState.musicInfo.duration.toFloat())
                )
            }
        }
    }

    private val playStateNullable
        get() = _playerUiStateFlow.value as? PlayerUiState.Active

    init {
        viewModelScope.launch {
            _playerUiStateFlow.collect {
                Log.d(TAG, ": playerUiStateFlow state $it")
            }
        }
        viewModelScope.launch {
            playListFlow.collect { musicInfoList ->
                if (musicInfoList.isNotEmpty()) {
                    playerRepository.setPlayList(musicInfoList.map { it.mediaItem })
                }
            }
        }
        viewModelScope.launch {
            interactingMusicItem.collect { musicInfo ->
                Log.d(TAG, "musicInfo11: $musicInfo")
                if (musicInfo == null) {
                    _playerUiStateFlow.value = PlayerUiState.Inactive
                } else {
                    _playerUiStateFlow.value = PlayerUiState.Active(
                        musicInfo = musicInfo,
                        state = when (playerRepository.playerState) {
                            is PlayerState.Paused -> PlayState.PAUSED
                            is PlayerState.Playing -> PlayState.PLAYING
                            else -> PlayState.LOADING
                        },
                        isFavorite = musicInFavorite.value.contains(musicInfo.contentUri.lastPathSegment!!.toLong())
                    )
                }
            }
        }
        viewModelScope.launch {
            playerRepository.observePlayerState().collect { state ->
                Log.d(TAG, "observePlayerState : $state")
                when (state) {
                    is PlayerState.Playing -> {
                        (_playerUiStateFlow.value as? PlayerUiState.Active)?.let { playerState ->
                            _playerUiStateFlow.update {
                                playerState.copy(
                                    state = PlayState.PLAYING
                                )
                            }
                        }
                    }
                    is PlayerState.Paused, PlayerState.PlayBackEnd -> {
                        (_playerUiStateFlow.value as? PlayerUiState.Active)?.let { playerState ->
                            _playerUiStateFlow.update {
                                playerState.copy(
                                    state = PlayState.PAUSED
                                )
                            }
                        }
                    }
                    is PlayerState.Error, PlayerState.Idle, PlayerState.PlayBackEnd -> {
                        _playerUiStateFlow.update { PlayerUiState.Inactive }
                    }
                    PlayerState.Buffering -> {
                        Log.d(TAG, "EEEEEEEEEEEEEEEE: ")
                        (_playerUiStateFlow.value as? PlayerUiState.Active)?.let { playerState ->
                            _playerUiStateFlow.update {
                                playerState.copy(
                                    state = PlayState.LOADING
                                )
                            }
                        }
                    }
                }
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

    fun onPlayMusic(playList: List<MusicInfo>) {
        onPlayMusic(playList, 0)
    }

    fun onPlayMusic(playList: List<MusicInfo>, index: Int) {
        when {
            playList != this.playListFlow.value -> {
                // Play list changed.
                savedStateHandle[PLAY_LIST_KEY] = playList
                playerRepository.seekToMediaIndex(index)
                playerRepository.play()
            }
            index != playListFlow.value.indexOf(interactingMusicItem.value) -> {
                // Play list is same but play item changed.
                playerRepository.seekToMediaIndex(index)
                playerRepository.play()
            }
        }
    }

    fun togglePlayState() {
        playStateNullable?.let {
            when (it.state) {
                PlayState.PAUSED -> playerRepository.play()
                PlayState.PLAYING -> playerRepository.pause()
                else -> Unit
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

    companion object {
        private const val PLAY_LIST_KEY = "play_list_key"
    }
}

sealed class PlayerUiState {
    object Inactive : PlayerUiState()

    data class Active(
        val state: PlayState = PlayState.LOADING,
        val progress: Float = 0f,
        val isFavorite: Boolean,
        val musicInfo: MusicInfo
    ) : PlayerUiState()
}

enum class PlayState {
    PAUSED,
    PLAYING,
    LOADING
}
