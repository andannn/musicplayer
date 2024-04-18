package com.andanana.musicplayer.feature.playList

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaBrowser
import com.andanana.musicplayer.core.data.repository.PlayerStateRepository
import com.andanana.musicplayer.core.data.util.getChildrenById
import com.andanana.musicplayer.core.data.util.getOrNull
import com.andanana.musicplayer.core.data.util.playMediaList
import com.andanana.musicplayer.core.model.LibraryRootCategory
import com.andanana.musicplayer.feature.playList.navigation.MEDIA_ID
import com.andannn.musicplayer.common.drawer.BottomSheetController
import com.andannn.musicplayer.common.drawer.BottomSheetModel
import com.andannn.musicplayer.common.drawer.SheetItem
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface PlayListEvent {
    data class OnStartPlayAtIndex(
        val mediaItems: List<MediaItem>,
        val index: Int,
    ) : PlayListEvent

    data class OnPlayAllButtonClick(
        val mediaItems: List<MediaItem>,
    ) : PlayListEvent

    data class OnShuffleButtonClick(
        val mediaItems: List<MediaItem>,
    ) : PlayListEvent

    data class OnOptionClick(
        val mediaItem: MediaItem,
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
    private val browserFuture: ListenableFuture<MediaBrowser>,
    private val bottomSheetController: BottomSheetController,
) : ViewModel() {
    private val mediaId =
        savedStateHandle.get<String>(MEDIA_ID) ?: ""

    private val _state = MutableStateFlow(PlayListUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            // wait browser build complete.
            val browser = browserFuture.await()

            val parentItem = browser.getItem(mediaId).await().value ?: return@launch

            val (type, _) =
                LibraryRootCategory.getMatchedChildTypeAndId(parentItem.mediaId)
                    ?: return@launch

            val playableItems =
                browser.getChildrenById(mediaId).run {
                    sortedBy {
                        it.mediaMetadata.trackNumber
                    }
                }

            _state.update {
                it.copy(
                    playListType = type,
                    parentItem = parentItem,
                    title = parentItem.mediaMetadata.title.toString(),
                    artCoverUri = parentItem.mediaMetadata.artworkUri ?: Uri.EMPTY,
                    trackCount = parentItem.mediaMetadata.totalTrackCount ?: 0,
                    musicItems = playableItems,
                )
            }
        }

        viewModelScope.launch {
            playerMonitor.playingMediaStateFlow.collect { playingMediaItem ->
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
                setPlayListAndStartIndex(event.mediaItems, event.index)
            }

            is PlayListEvent.OnPlayAllButtonClick -> {
                setPlayListAndStartIndex(event.mediaItems, 0)
            }

            is PlayListEvent.OnShuffleButtonClick -> {
                setPlayListAndStartIndex(event.mediaItems, 0, isShuffle = true)
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
                bottomSheetController.onRequestShowSheet(state.value.parentItem)
            }
        }
    }

    private fun setPlayListAndStartIndex(
        mediaItems: List<MediaItem>,
        index: Int,
        isShuffle: Boolean = false,
    ) {
        browserFuture.getOrNull()?.run {
            shuffleModeEnabled = isShuffle
            playMediaList(mediaItems, index)
        }
    }
}

data class PlayListUiState(
    val playListType: LibraryRootCategory = LibraryRootCategory.MINE_PLAYLIST,
    val parentItem: MediaItem = MediaItem.EMPTY,
    val title: String = "",
    val artCoverUri: Uri = Uri.EMPTY,
    val trackCount: Int = 0,
    val musicItems: List<MediaItem> = emptyList(),
    val playingMediaItem: MediaItem? = null,
    val contentUri: Uri = Uri.EMPTY,
)
