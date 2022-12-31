package com.andanana.musicplayer.feature_music_list.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.feature_music_list.domain.repository.LocalMusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalMusicViewModel @Inject constructor(
    private val localMusicRepository: LocalMusicRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        viewModelScope.launch {
            localMusicRepository.getLocalMusicItems().let {
                Log.d("NEU", ": $it")
            }
        }
        Log.d("TTT", ": ${localMusicRepository.hashCode()}")
    }
}
