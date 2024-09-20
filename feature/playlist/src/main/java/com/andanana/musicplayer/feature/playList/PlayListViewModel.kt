package com.andanana.musicplayer.feature.playList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.domain.model.MediaItemModel
import com.andanana.musicplayer.core.domain.model.AudioItemModel
import com.andanana.musicplayer.core.domain.model.MediaListSource
import com.andanana.musicplayer.core.domain.repository.MediaControllerRepository
import com.andanana.musicplayer.core.domain.repository.PlayerStateRepository
import com.andanana.musicplayer.feature.playList.navigation.ID
import com.andanana.musicplayer.feature.playList.navigation.SOURCE
import com.andannn.musicplayer.common.drawer.BottomSheetController
import com.andannn.musicplayer.common.drawer.BottomSheetModel
import com.andannn.musicplayer.common.drawer.SheetItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
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
    private val playerStateRepository: PlayerStateRepository,
    private val mediaControllerRepository: MediaControllerRepository,
    private val bottomSheetController: BottomSheetController,
) : ViewModel() {
    private val id =
        savedStateHandle.get<String>(ID) ?: ""

    val mediaListSource =
        savedStateHandle.get<MediaListSource>(SOURCE) ?: MediaListSource.ALBUM

    private val _state = MutableStateFlow(PlayListUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            when (mediaListSource) {
                MediaListSource.ALBUM -> {
                    val albumItem =
                        mediaControllerRepository.getAlbumByAlbumId(id.toLong())
                            ?: error("Not found")
                    val playableItems = mediaControllerRepository.getAudiosOfAlbum(id.toLong())
                    _state.update {
                        it.copy(
                            headerInfoItem = albumItem,
                            audioList = playableItems.toImmutableList(),
                        )
                    }
                }

                MediaListSource.ARTIST -> {
                    val headerItem =
                        mediaControllerRepository.getArtistByAlbumId(id.toLong())
                            ?: error("Not found")

                    val playableItems = mediaControllerRepository.getAudiosOfArtist(id.toLong())
                    _state.update {
                        it.copy(
                            headerInfoItem = headerItem,
                            audioList = playableItems.toImmutableList(),
                        )
                    }
                }
            }
        }

        viewModelScope.launch {
            playerStateRepository.playingMediaStateFlow.collect { playingMediaItem ->
                _state.update {
                    it.copy(playingMediaItem = playingMediaItem)
                }
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
                bottomSheetController.onRequestShowSheet(event.mediaItem)
            }

            is PlayListEvent.OnDismissRequest -> {
                with(bottomSheetController) {
                    viewModelScope.onDismissRequest(event.item)
                }
            }

            PlayListEvent.OnHeaderOptionClick -> {
                state.value.headerInfoItem?.let {
                    bottomSheetController.onRequestShowSheet(it)
                }
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
    val headerInfoItem: MediaItemModel? = null,
    val audioList: ImmutableList<AudioItemModel> = emptyList<AudioItemModel>().toImmutableList(),
    val playingMediaItem: AudioItemModel? = null,
)
