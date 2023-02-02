package com.andanana.musicplayer.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.model.ArtistInfo
import com.andanana.musicplayer.feature.home.usecase.GetAllArtist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ArtistPageViewModel"

@HiltViewModel
class ArtistPageViewModel @Inject constructor(
    private val getAllArtist: GetAllArtist,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _artistPageUiState = MutableStateFlow<ArtistPageUiState>(ArtistPageUiState.Loading)
    val artistPageUiState = _artistPageUiState.asStateFlow()

    init {
        viewModelScope.launch {
            val artists = getAllArtist.invoke()
            _artistPageUiState.update {
                ArtistPageUiState.Ready(artists)
            }
        }
    }
}

sealed interface ArtistPageUiState {
    object Loading : ArtistPageUiState
    data class Ready(val infoList: List<ArtistInfo>) : ArtistPageUiState
}
