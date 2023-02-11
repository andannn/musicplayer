package com.andanana.musicplayer.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.model.MusicInfo
import com.andanana.musicplayer.feature.home.usecase.GetAllMusicItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AudioPageViewModel"

@HiltViewModel
class AudioPageViewModel @Inject constructor(
    private val getAllMusicItem: GetAllMusicItem,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _audioPageUiState = MutableStateFlow<AudioPageUiState>(AudioPageUiState.Loading)
    val audioPageUiState = _audioPageUiState.asStateFlow()

    init {
        viewModelScope.launch {
            val musics = getAllMusicItem.invoke().sortedBy { it.album }
            _audioPageUiState.update {
                AudioPageUiState.Ready(musics)
            }
        }
    }
}

sealed class AudioPageUiState {
    object Loading : AudioPageUiState()
    data class Ready(val infoList: List<MusicInfo>) : AudioPageUiState()
}
