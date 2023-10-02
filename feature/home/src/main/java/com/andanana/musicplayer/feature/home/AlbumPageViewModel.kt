package com.andanana.musicplayer.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.data.model.AlbumModel
import com.andanana.musicplayer.core.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumPageViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _albumPageUiState = MutableStateFlow<AlbumPageUiState>(AlbumPageUiState.Loading)
    val albumPageUiState = _albumPageUiState.asStateFlow()

    init {
        viewModelScope.launch {
            musicRepository.sync()
        }
        viewModelScope.launch {
            musicRepository.getAllAlbums()
                .distinctUntilChanged()
                .collect { albums ->
                    _albumPageUiState.update {
                        AlbumPageUiState.Ready(albums)
                    }
                }
        }
    }
}

sealed interface AlbumPageUiState {
    object Loading : AlbumPageUiState
    data class Ready(val infoList: List<AlbumModel>) : AlbumPageUiState
}
