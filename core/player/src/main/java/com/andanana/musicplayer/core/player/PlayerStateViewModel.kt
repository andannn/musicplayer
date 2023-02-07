package com.andanana.musicplayer.core.player

import android.app.Application
import android.provider.MediaStore.Audio.Media
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.andanana.musicplayer.core.model.MusicInfo
import com.andanana.musicplayer.core.player.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PlayerStateViewModel"

@HiltViewModel
class PlayerStateViewModel @Inject constructor(
    private val application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val playerRepository: PlayerRepository
) : ViewModel() {

    private val playListFlow =
        savedStateHandle.getStateFlow<List<MusicInfo>>(PLAY_LIST_KEY, emptyList())

    init {
        viewModelScope.launch {
            playerRepository.observePlayerState().collect {
                Log.d(TAG, ": player state $it")
            }
        }
    }

    fun setPlayList(playList: List<MusicInfo>) {
        savedStateHandle[PLAY_LIST_KEY] = playList
    }

    companion object {
        private const val PLAY_LIST_KEY = "play_list_key"
    }
}
