package com.andanana.musicplayer.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.model.AlbumInfo
import com.andanana.musicplayer.feature.home.usecase.GetAllAlbum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AlbumPageViewModel"

@HiltViewModel
class AlbumPageViewModel @Inject constructor(
    private val getAllAlbum: GetAllAlbum,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _albumPageUiState = MutableStateFlow<AlbumPageUiState>(AlbumPageUiState.Loading)
    val albumPageUiState = _albumPageUiState.asStateFlow()

    init {
        viewModelScope.launch {
            val albums = getAllAlbum.invoke()
            _albumPageUiState.update {
                AlbumPageUiState.Ready(albums)
            }
        }
    }
}

sealed interface AlbumPageUiState {
    object Loading : AlbumPageUiState
    data class Ready(val infoList: List<AlbumInfo>) : AlbumPageUiState
}
