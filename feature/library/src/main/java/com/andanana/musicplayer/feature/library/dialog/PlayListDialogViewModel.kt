package com.andanana.musicplayer.feature.library

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.database.entity.PlayList
import com.andanana.musicplayer.core.database.usecases.PlayListUseCases
import com.andanana.musicplayer.core.model.RequestType
import com.andanana.musicplayer.core.model.RequestType.Companion.toUri
import com.andanana.musicplayer.feature.library.navigation.requestUriLastSegmentArg
import com.andanana.musicplayer.feature.library.navigation.requestUriTypeArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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

    private val savedPlayListItem =
        useCases.getPlayListsOfMusic(requestUriLastSegmentFlow.value.toLong())
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val readyStateOrNull
        get() = (_uiState.value as? PlayListDialogUiState.Ready)

    init {
        viewModelScope.launch {
            _uiState.collect {
                Log.d(TAG, ": a $it")
            }
        }
        viewModelScope.launch {
            combine(
                useCases.getAllPlayList(),
                savedPlayListItem
            ) { allPlayList, checkedPlayList ->
                allPlayList to checkedPlayList
            }.collect { (allPlayList, checkedPlayList) ->
                _uiState.value = PlayListDialogUiState.Ready(
                    playListItems = allPlayList
                        .sortedByDescending { it.createdDate }
                        .map { it.matToUiData() },
                    checkItemList = checkedPlayList
                        .map { it.matToUiData() }
                )
            }
        }
    }

    fun onItemCheckChange(item: PlayListItem, checked: Boolean) {
        readyStateOrNull?.let { state ->
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

    fun onApplyButtonClick() {
        val unSavedPlayListItem =
            readyStateOrNull?.checkItemList?.subtract(
                savedPlayListItem.value.map { it.matToUiData() }.toSet()
            )
                ?.toList() ?: emptyList()

        viewModelScope.launch {
            unSavedPlayListItem.forEach {
                useCases.addMusicToPlayList.invoke(
                    musicMediaId = requestUriLastSegmentFlow.value.toLong(),
                    playlistId = it.id,
                    addedDate = System.currentTimeMillis()
                )
            }
        }
    }
}

sealed interface PlayListDialogUiState {
    object Loading : PlayListDialogUiState

    data class Ready(
        val checkItemList: List<PlayListItem> = emptyList(),
        val playListItems: List<PlayListItem>
    ) : PlayListDialogUiState
}
