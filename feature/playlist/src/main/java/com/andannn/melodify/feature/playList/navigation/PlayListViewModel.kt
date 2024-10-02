package com.andannn.melodify.feature.playList.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andannn.melodify.core.domain.model.MediaItemModel
import com.andannn.melodify.core.domain.model.AudioItemModel
import com.andannn.melodify.core.domain.model.MediaListSource
import com.andannn.melodify.core.domain.repository.MediaControllerRepository
import com.andannn.melodify.core.domain.repository.PlayerStateRepository
import com.andannn.melodify.common.drawer.BottomSheetController
import com.andannn.melodify.common.drawer.SheetItem
import com.andannn.melodify.core.domain.repository.MediaContentObserverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
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

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PlayListViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val contentObserverRepository: MediaContentObserverRepository,
    playerStateRepository: PlayerStateRepository,
    private val mediaControllerRepository: MediaControllerRepository,
    private val bottomSheetController: BottomSheetController,
) : ViewModel() {
    private val id =
        savedStateHandle.get<String>(ID) ?: ""

    val mediaListSource =
        savedStateHandle.get<MediaListSource>(SOURCE) ?: MediaListSource.ALBUM

    private val contentUri
        get() = with(contentObserverRepository) {
            when (mediaListSource) {
                MediaListSource.ALBUM -> getAlbumUri(id.toLong())
                MediaListSource.ARTIST -> getArtistUri(id.toLong())
            }
        }

    private val playListContentFlow =
        contentObserverRepository.getContentChangedEventFlow(contentUri)
            .mapLatest { _ ->
                getPlayListContent()
            }

    private val playingItemFlow = playerStateRepository.playingMediaStateFlow

    val state = combine(
        playListContentFlow,
        playingItemFlow,
    ) { content, playingItem ->
        PlayListUiState(
            headerInfoItem = content.headerInfoItem,
            audioList = content.audioList,
            playingMediaItem = playingItem,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), PlayListUiState())

    private suspend fun getPlayListContent(): PlayListContent {
        return when (mediaListSource) {
            MediaListSource.ALBUM -> {
                val albumItem =
                    mediaControllerRepository.getAlbumByAlbumId(id.toLong())
                val playableItems = mediaControllerRepository.getAudiosOfAlbum(id.toLong())
                    .sortedBy { it.cdTrackNumber }
                PlayListContent(
                    headerInfoItem = albumItem,
                    audioList = playableItems.toImmutableList(),
                )
            }

            MediaListSource.ARTIST -> {
                val headerItem =
                    mediaControllerRepository.getArtistByAlbumId(id.toLong())

                val playableItems = mediaControllerRepository.getAudiosOfArtist(id.toLong())
                PlayListContent(
                    headerInfoItem = headerItem,
                    audioList = playableItems.toImmutableList(),
                )
            }
        }
    }

    fun onEvent(event: PlayListEvent) {
        when (event) {
            is PlayListEvent.OnStartPlayAtIndex -> {
                setPlayListAndStartIndex(state.value.audioList, event.index)
            }

            is PlayListEvent.OnPlayAllButtonClick -> {
                setPlayListAndStartIndex(state.value.audioList, 0)
            }

            is PlayListEvent.OnShuffleButtonClick -> {
                setPlayListAndStartIndex(state.value.audioList, 0, isShuffle = true)
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
        mediaControllerRepository.playMediaList(mediaItems, index)
    }
}

private data class PlayListContent(
    val headerInfoItem: MediaItemModel? = null,
    val audioList: ImmutableList<AudioItemModel> = emptyList<AudioItemModel>().toImmutableList(),
)

data class PlayListUiState(
    val headerInfoItem: MediaItemModel? = null,
    val audioList: ImmutableList<AudioItemModel> = emptyList<AudioItemModel>().toImmutableList(),
    val playingMediaItem: AudioItemModel? = null,
)
