package com.andanana.musicplayer.core.player

import android.app.Application
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    init {
        viewModelScope.launch {
            playerRepository.observePlayerState().collect {
                Log.d(TAG, ": player state $it")
            }
        }
    }
}
