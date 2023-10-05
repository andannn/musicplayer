package com.andanana.musicplayer.feature.playList

import android.app.Application
import android.content.ComponentName
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionToken
import com.andanana.musicplayer.core.player.PlayerController
import com.andanana.musicplayer.feature.playList.navigation.MediaIdKey
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

private const val TAG = "PlayListViewModel"

@HiltViewModel
class PlayListViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val playerController: PlayerController,
) : ViewModel() {

    private val mediaId =
        savedStateHandle.get<String>(MediaIdKey) ?: ""

    private val _state = MutableStateFlow(PlayListUiState())
    val state = _state.asStateFlow()

    private var browserFuture: ListenableFuture<MediaBrowser> = MediaBrowser.Builder(
        application,
        SessionToken(
            application,
            ComponentName(application, "com.andanana.musicplayer.PlayerService")
        )
    )
        .buildAsync()

    private val browser: MediaBrowser?
        get() = if (browserFuture.isDone && !browserFuture.isCancelled) browserFuture.get() else null

    init {
        viewModelScope.launch {
            // wait browser build complete.
            val browser = browserFuture.await()

            val parentItem = browser.getItem(mediaId).await().value ?: return@launch

            val playableItems = browser.getChildren(
                mediaId,
                /* page= */ 0,
                /* pageSize= */ Int.MAX_VALUE,
                /* params= */ null
            ).await().value!!.toList()

            Log.d(TAG, ": $mediaId")
            Log.d(TAG, ": $playableItems")
            _state.update {
                PlayListUiState(
                    title = parentItem.mediaMetadata.title.toString(),
                    artCoverUri = parentItem.mediaMetadata.artworkUri ?: Uri.EMPTY,
                    trackCount = parentItem.mediaMetadata.totalTrackCount ?: 0,
                    musicItems = playableItems
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        MediaBrowser.releaseFuture(browserFuture)
        browser?.release()
    }

    fun setPlayListAndStartIndex(mediaItems: List<MediaItem>, index: Int) {
        browser?.run {
            setMediaItems(
                mediaItems,
                /* startIndex= */ index,
                /* startPositionMs= */ C.TIME_UNSET
            )
            shuffleModeEnabled = false
            prepare()
            play()
        }
    }
}

data class PlayListUiState(
    val title: String = "",
    val artCoverUri: Uri = Uri.EMPTY,
    val trackCount: Int = 0,
    val musicItems: List<MediaItem> = emptyList(),
//    val playingMediaItem: MediaItem? = null,
    val contentUri: Uri = Uri.EMPTY
)
