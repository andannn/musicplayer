package com.andanana.musicplayer.feature.library

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.database.usecases.PlayListUseCases
import com.andanana.musicplayer.core.model.RequestType
import com.andanana.musicplayer.core.model.RequestType.Companion.toUri
import com.andanana.musicplayer.feature.library.navigation.requestUriLastSegmentArg
import com.andanana.musicplayer.feature.library.navigation.requestUriTypeArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PlayListDialogViewModel"

@HiltViewModel
class PlayListDialogViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCases: PlayListUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlayListDialogUiState>(PlayListDialogUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val requestUriLastSegmentFlow =
        savedStateHandle.getStateFlow(requestUriLastSegmentArg, "")
    private val requestTypeFlow =
        savedStateHandle.getStateFlow(requestUriTypeArg, RequestType.MUSIC_REQUEST)
    private val requestUri = combine(
        requestTypeFlow,
        requestUriLastSegmentFlow
    ) { type, lastSegment ->
        type.toUri(lastSegment)
    }

    init {
        viewModelScope.launch {
            _uiState.collect {
                Log.d(TAG, ": a $it")
            }
        }
        viewModelScope.launch {
            combine(
                useCases.getAllPlayList(),
                useCases.getPlayListsOfMusic(requestUriLastSegmentFlow.value.toLong())
            ) { allPlayList, checkedPlayList ->
                allPlayList to checkedPlayList
            }.collect { (allPlayList, checkedPlayList) ->
                _uiState.value = PlayListDialogUiState.Ready(
                    playListItems = allPlayList.map { PlayListItem(it.name) },
                    checkItemList = checkedPlayList.map { PlayListItem(it.name) }
                )
            }
        }
    }

    fun onItemCheckChange(item: PlayListItem, checked: Boolean) {
        (_uiState.value as? PlayListDialogUiState.Ready)?.let { state ->
            _uiState.update {
                state.copy(
                    checkItemList = state.checkItemList.toMutableList().apply {
                        if (checked) {
                            add(item)
                        } else {
                            remove(item)
                        }
                    }
                )
            }
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
