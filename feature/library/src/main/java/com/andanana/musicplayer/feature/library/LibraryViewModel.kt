package com.andanana.musicplayer.feature.library

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.database.usecases.PlayListUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCases: PlayListUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow<LibraryUiState>(LibraryUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            useCases.getAllPlayList.invoke().collect { list ->
                _uiState.update {
                    LibraryUiState.Ready(
                        playList = list.matToUiData()
                    )
                }
            }
        }
    }
}

sealed interface LibraryUiState {
    object Loading : LibraryUiState
    data class Ready(val playList: List<PlayListItem>) : LibraryUiState
}
