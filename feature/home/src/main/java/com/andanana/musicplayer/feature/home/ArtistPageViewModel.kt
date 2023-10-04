package com.andanana.musicplayer.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.data.model.ArtistModel
import com.andanana.musicplayer.core.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ArtistPageViewModel"

@HiltViewModel
class ArtistPageViewModel @Inject constructor(
    musicRepository: MusicRepository
) : ViewModel() {

    private val _artistPageUiState = MutableStateFlow<ArtistPageUiState>(ArtistPageUiState.Loading)
    val artistPageUiState = _artistPageUiState.asStateFlow()

    init {
        viewModelScope.launch {
            musicRepository.getAllArtists()
                .distinctUntilChanged()
                .collect { artistModels ->
                    _artistPageUiState.update {
                        ArtistPageUiState.Ready(artistModels)
                    }
                }
        }
    }
}

sealed interface ArtistPageUiState {
    object Loading : ArtistPageUiState
    data class Ready(val infoList: List<ArtistModel>) : ArtistPageUiState
}
