package com.andanana.musicplayer.feature.playList

import android.app.Application
import android.content.ComponentName
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionToken
import com.andanana.musicplayer.core.data.repository.PlayerStateRepository
import com.andanana.musicplayer.core.model.LibraryRootCategory
import com.andanana.musicplayer.feature.playList.navigation.MEDIA_ID
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
}

@HiltViewModel
class PlayListViewModel
    @Inject
    constructor(
        application: Application,
        savedStateHandle: SavedStateHandle,
        private val playerMonitor: PlayerStateRepository,
    ) : ViewModel() {
        private val mediaId =
            savedStateHandle.get<String>(MEDIA_ID) ?: ""

        private val _state = MutableStateFlow(PlayListUiState())
        val state = _state.asStateFlow()

        private var browserFuture: ListenableFuture<MediaBrowser> =
            MediaBrowser.Builder(
                application,
                SessionToken(
                    application,
                    ComponentName(application, "com.andanana.musicplayer.PlayerService"),
                ),
            )
                .buildAsync()

        private val browser: MediaBrowser?
            get() = if (browserFuture.isDone && !browserFuture.isCancelled) browserFuture.get() else null

        init {
            viewModelScope.launch {
                // wait browser build complete.
                val browser = browserFuture.await()

                val parentItem = browser.getItem(mediaId).await().value ?: return@launch

                val (type, _) =
                    LibraryRootCategory.getMatchedChildTypeAndId(parentItem.mediaId)
                        ?: return@launch

                val playableItems =
                    browser.getChildren(
                        mediaId,
                        0,
                        Int.MAX_VALUE,
                        null,
                    ).await().value!!.toList().run {
                        sortedBy {
                            it.mediaMetadata.trackNumber
                        }
                    }

                _state.update {
                    it.copy(
                        playListType = type,
                        title = parentItem.mediaMetadata.title.toString(),
                        artCoverUri = parentItem.mediaMetadata.artworkUri ?: Uri.EMPTY,
                        trackCount = parentItem.mediaMetadata.totalTrackCount ?: 0,
                        musicItems = playableItems,
                    )
                }
            }

            viewModelScope.launch {
                playerMonitor.observePlayingMedia().collect { playingMediaItem ->
                    _state.update {
                        it.copy(playingMediaItem = playingMediaItem)
                    }
                }
            }
        }

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
            }
        }

        private fun setPlayListAndStartIndex(
            mediaItems: List<MediaItem>,
            index: Int,
            isShuffle: Boolean = false,
        ) {
            browser?.run {
                setMediaItems(
                    mediaItems,
                    // startIndex=
                    index,
                    // startPositionMs=
                    C.TIME_UNSET,
                )
                shuffleModeEnabled = isShuffle
                prepare()
                play()
            }
        }

        override fun onCleared() {
            super.onCleared()
            MediaBrowser.releaseFuture(browserFuture)
            browser?.release()
        }
    }

data class PlayListUiState(
    val playListType: LibraryRootCategory = LibraryRootCategory.MINE_PLAYLIST,
    val title: String = "",
    val artCoverUri: Uri = Uri.EMPTY,
    val trackCount: Int = 0,
    val musicItems: List<MediaItem> = emptyList(),
    val playingMediaItem: MediaItem? = null,
    val contentUri: Uri = Uri.EMPTY,
)
