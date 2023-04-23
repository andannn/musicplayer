package com.andanana.musicplayer.feature.library.dialog

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.database.usecases.PlayListUseCases
import com.andanana.musicplayer.core.model.RequestType
import com.andanana.musicplayer.core.model.RequestType.Companion.toUri
import com.andanana.musicplayer.feature.library.PlayListItem
import com.andanana.musicplayer.feature.library.isFavoritePlayList
import com.andanana.musicplayer.feature.library.matToUiData
import com.andanana.musicplayer.feature.library.navigation.requestUriLastSegmentArg
import com.andanana.musicplayer.feature.library.navigation.requestUriTypeArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PlayListDialogViewModel"

@HiltViewModel
class PlayListDialogViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCases: PlayListUseCases
) : ViewModel() {

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

    private val checkItemListFlow =
        useCases.getPlayListsOfMusic(requestUriLastSegmentFlow.value.toLong())
            .map { it.matToUiData() }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    private val allPlayListFlow =
        useCases.getAllPlayList()
            .map {
                it.matToUiData()
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val uiState = combine(
        allPlayListFlow,
        checkItemListFlow
    ) { playListItems, checkItemList ->
        PlayListDialogUiState.Ready(
            playListItems = playListItems,
            checkItemList = checkItemList
        )
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), PlayListDialogUiState.Loading)

    fun onItemCheckChange(item: PlayListItem, checked: Boolean) {
        viewModelScope.launch {
            if (checked) {
                useCases.addMusicToPlayList.invoke(
                    musicMediaId = requestUriLastSegmentFlow.value.toLong(),
                    playlistId = item.id,
                    addedDate = System.currentTimeMillis()
                )
            } else {
                useCases.deleteMusicInPlayList.invoke(
                    musicMediaId = requestUriLastSegmentFlow.value.toLong(),
                    playListId = item.id
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
    ) : PlayListDialogUiState {
        val favoriteItem = playListItems.find { it.isFavoritePlayList }
        val playListItemWithoutFavorite = playListItems.toMutableList()
            .apply {
                removeIf { it.isFavoritePlayList }
            }
            .toList()
    }
}
