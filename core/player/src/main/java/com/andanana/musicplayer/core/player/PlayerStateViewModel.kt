package com.andanana.musicplayer.core.player

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.model.MusicInfo
import com.andanana.musicplayer.core.player.repository.PlayerEvent
import com.andanana.musicplayer.core.player.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PlayerStateViewModel"

@HiltViewModel
class PlayerStateViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val playerRepository: PlayerRepository
) : ViewModel() {

    private val playListFlow =
        savedStateHandle.getStateFlow<List<MusicInfo>>(PLAY_LIST_KEY, emptyList())

    private val interactingMusicItem: StateFlow<MusicInfo?> =
        combine(
            playerRepository.observePlayingMediaItem(),
            playListFlow
        ) { playingMediaItem, playList ->
            playList.find {
                it.mediaItem == playingMediaItem
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        viewModelScope.launch {
            playListFlow.collect { musicInfoList ->
                if (musicInfoList.isNotEmpty()) {
                    playerRepository.setPlayList(musicInfoList.map { it.mediaItem })
                }
            }
        }
        viewModelScope.launch {
            interactingMusicItem.collect {
                Log.d(TAG, ": player interactingMusicItem $it")
            }
        }
        viewModelScope.launch {
            playerRepository.observePlayerState().collect {
                Log.d(TAG, ": player state $it")
            }
        }
    }

    fun onEvent(event: PlayerEvent) {
        when (event) {
            is PlayerEvent.OnPlayMusicInPlayList -> {
                when {
                    event.playList != this.playListFlow.value -> {
                        // Play list changed.
                        savedStateHandle[PLAY_LIST_KEY] = event.playList
                        playerRepository.seekToMediaIndex(event.index)
                    }
                    event.index != playListFlow.value.indexOf(interactingMusicItem.value) -> {
                        // Play list is same but play item changed.
                        playerRepository.seekToMediaIndex(event.index)
                    }
                }
            }
        }
    }

    companion object {
        private const val PLAY_LIST_KEY = "play_list_key"
    }
}
