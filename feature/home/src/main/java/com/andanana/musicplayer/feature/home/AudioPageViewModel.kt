package com.andanana.musicplayer.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.data.model.MusicModel
import com.andanana.musicplayer.core.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AudioPageViewModel"

@HiltViewModel
class AudioPageViewModel @Inject constructor(
    musicRepository: MusicRepository
) : ViewModel() {

    private val _audioPageUiState = MutableStateFlow<AudioPageUiState>(AudioPageUiState.Loading)
    val audioPageUiState = _audioPageUiState.asStateFlow()

    init {
        viewModelScope.launch {
            musicRepository.getAllMusics()
                .distinctUntilChanged()
                .collect { musics ->
                    musics.onEach {
                        Log.d(TAG, "$it: ")
                    }
                    _audioPageUiState.update {
                        AudioPageUiState.Ready(musics)
                    }
                }
        }
    }
}

sealed class AudioPageUiState {
    object Loading : AudioPageUiState()
    data class Ready(val infoList: List<MusicModel>) : AudioPageUiState()
}
