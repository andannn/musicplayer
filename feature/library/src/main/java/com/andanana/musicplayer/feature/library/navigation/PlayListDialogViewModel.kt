package com.andanana.musicplayer.feature.library.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.database.usecases.PlayListUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayListDialogViewModel @Inject constructor(
    private val useCases: PlayListUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlayListDialogUiState>(PlayListDialogUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            useCases.getAllPlayList().onEach { list ->
                _uiState.value = PlayListDialogUiState.Ready(
                    playListItems = list.map { PlayListItem(it.name) }
                )
            }.collect()
        }
    }
}

data class PlayListItem(
    val name: String
)

sealed interface PlayListDialogUiState {
    object Loading : PlayListDialogUiState

    data class Ready(
        val checkItemList: List<PlayListItem> = emptyList(),
        val playListItems: List<PlayListItem>
    ) : PlayListDialogUiState
}
