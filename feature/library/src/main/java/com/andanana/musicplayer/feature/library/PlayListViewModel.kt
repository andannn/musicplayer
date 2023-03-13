package com.andanana.musicplayer.feature.library

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.database.usecases.PlayListUseCases
import com.andanana.musicplayer.core.model.MusicInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCases: PlayListUseCases
) : ViewModel() {
    private val _playListPageUiState = MutableStateFlow<PlayListPageUiState>(PlayListPageUiState.Loading)
    val playListPageUiState = _playListPageUiState.asStateFlow()

    init {
        viewModelScope.launch {
            useCases.getAllPlayList.invoke().onEach { res ->
                _playListPageUiState.update {
                    PlayListPageUiState.Ready(res.matToUiData())
                }
            }.collect()
        }
    }
}

sealed interface PlayListPageUiState {
    object Loading : PlayListPageUiState
    data class Ready(val infoList: List<PlayListItem>) : PlayListPageUiState
}

