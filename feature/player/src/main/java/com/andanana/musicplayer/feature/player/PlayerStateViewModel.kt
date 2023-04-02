package com.andanana.musicplayer.feature.player

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.data.repository.LocalMusicRepository
import com.andanana.musicplayer.core.database.usecases.PlayListUseCases
import com.andanana.musicplayer.core.model.MusicInfo
import com.andanana.musicplayer.core.model.PlayMode
import com.andanana.musicplayer.core.player.repository.PlayerRepository
import com.andanana.musicplayer.core.player.repository.PlayerState
import com.andanana.musicplayer.core.player.util.CoroutineTicker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PlayerStateViewModel"

@HiltViewModel
class PlayerStateViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val localMusicRepository: LocalMusicRepository,
    private val useCases: PlayListUseCases
) : ViewModel() {

    private val interactingMusicItem: StateFlow<MusicInfo?> =
        playerRepository.observePlayingUri()
            .map { uri ->
                uri?.lastPathSegment?.toLong()?.let {
                    localMusicRepository.getMusicInfoById(it)
                }
            }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

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
            musicInFavorite.collect {
                playStateNullable?.let { playerState ->
                    _playerUiStateFlow.update {
                        playerState.copy(
                            isFavorite = isMusicInFavorite(playerState.musicInfo.contentUri)
                        )
                    }
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
                        isFavorite = isMusicInFavorite(musicInfo.contentUri)
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

    private fun isMusicInFavorite(uri: Uri) =
        musicInFavorite.value.map {
            it.music.mediaStoreId
        }.contains(uri.lastPathSegment!!.toLong())
}

sealed class PlayerUiState {
    object Inactive : PlayerUiState()

    data class Active(
        val state: PlayState = PlayState.LOADING,
        val progress: Float = 0f,
        val isFavorite: Boolean,
        val playMode: PlayMode = PlayMode.REPEAT_ALL,
        val musicInfo: MusicInfo
    ) : PlayerUiState()
}

enum class PlayState {
    PAUSED,
    PLAYING,
    LOADING
}
