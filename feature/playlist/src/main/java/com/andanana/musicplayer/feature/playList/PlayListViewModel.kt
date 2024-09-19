package com.andanana.musicplayer.feature.playList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.domain.model.AlbumItemModel
import com.andanana.musicplayer.core.domain.model.MediaItemModel
import com.andanana.musicplayer.core.domain.model.AudioItemModel
import com.andanana.musicplayer.core.domain.repository.MediaControllerRepository
import com.andanana.musicplayer.core.domain.repository.PlayerStateRepository
import com.andanana.musicplayer.feature.playList.navigation.ID
import com.andannn.musicplayer.common.drawer.BottomSheetController
import com.andannn.musicplayer.common.drawer.BottomSheetModel
import com.andannn.musicplayer.common.drawer.SheetItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface PlayListEvent {
    data class OnStartPlayAtIndex(
        val index: Int,
    ) : PlayListEvent

    data object OnPlayAllButtonClick : PlayListEvent

    data object OnShuffleButtonClick : PlayListEvent

    data class OnOptionClick(
        val mediaItem: MediaItemModel,
    ) : PlayListEvent

    data object OnHeaderOptionClick : PlayListEvent

    data class OnDismissRequest(val item: SheetItem?) : PlayListEvent

}

@HiltViewModel
class PlayListViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val playerMonitor: PlayerStateRepository,
    private val mediaControllerRepository: MediaControllerRepository,
    private val bottomSheetController: BottomSheetController,
) : ViewModel() {
    private val id =
        savedStateHandle.get<String>(ID) ?: ""

    private val _state = MutableStateFlow(PlayListUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val albumItem =
                mediaControllerRepository.getAlbumByAlbumId(id.toLong()) ?: error("Not found")
            val playableItems = mediaControllerRepository.getAudiosOfAlbum(id.toLong())
            _state.update {
                it.copy(
                    album = albumItem,
                    audioList = playableItems,
                )
            }
        }

        viewModelScope.launch {
            playerMonitor.playingMediaStateFlow.collect { playingMediaItem ->
//                _state.update {
//                    it.copy(playingMediaItem = playingMediaItem)
//                }
            }
        }
    }

    val bottomSheetModel: StateFlow<BottomSheetModel?>
        get() = bottomSheetController.bottomSheetModel

    fun onEvent(event: PlayListEvent) {
        when (event) {
            is PlayListEvent.OnStartPlayAtIndex -> {
                setPlayListAndStartIndex(_state.value.audioList, event.index)
            }

            is PlayListEvent.OnPlayAllButtonClick -> {
                setPlayListAndStartIndex(_state.value.audioList, 0)
            }

            is PlayListEvent.OnShuffleButtonClick -> {
                setPlayListAndStartIndex(_state.value.audioList, 0, isShuffle = true)
            }

            is PlayListEvent.OnOptionClick -> {
//                bottomSheetController.onRequestShowSheet(event.mediaItem)
            }

            is PlayListEvent.OnDismissRequest -> {
                with(bottomSheetController) {
                    viewModelScope.onDismissRequest(event.item)
                }
            }

            PlayListEvent.OnHeaderOptionClick -> {
//                bottomSheetController.onRequestShowSheet(state.value.parentItem)
            }
        }
    }

    private fun setPlayListAndStartIndex(
        mediaItems: List<AudioItemModel>,
        index: Int,
        isShuffle: Boolean = false,
    ) {
        mediaControllerRepository.playMediaList(mediaItems, index, isShuffle)
    }
}

data class PlayListUiState(
    val album: AlbumItemModel = AlbumItemModel.DEFAULT,
    val audioList: List<AudioItemModel> = emptyList(),
    val playingMediaItem: AudioItemModel? = null,
)
