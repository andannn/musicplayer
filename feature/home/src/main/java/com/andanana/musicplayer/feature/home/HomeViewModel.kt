package com.andanana.musicplayer.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaBrowser
import com.andanana.musicplayer.core.data.ContentChangeFlowProvider
import com.andanana.musicplayer.core.model.ALL_MUSIC_ID
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val browserFuture: ListenableFuture<MediaBrowser>,
        private val contentChangeFlowProvider: ContentChangeFlowProvider,
    ) : ViewModel() {
        private val browser: MediaBrowser?
            get() = if (browserFuture.isDone && !browserFuture.isCancelled) browserFuture.get() else null

        private val _state = MutableStateFlow(HomeUiState())
        val state = _state.asStateFlow()

        private var queryJob: Job? = null

        init {
            viewModelScope.launch {
                state.collect {
                    Log.d(TAG, "$it: ")
                }
            }
            viewModelScope.launch {
                // wait browser build complete.
                val browser = browserFuture.await()

                val root = browser.getLibraryRoot(null).await()
                val categories =
                    browser.getChildren(
                        root.value!!.mediaId,
                        0,
                        Int.MAX_VALUE,
                        null,
                    ).await().value!!.toList()

                _state.update {
                    HomeUiState(
                        categories = categories,
                    )
                }

                contentChangeFlowProvider.audioChangedEventFlow.collect { _ ->
                    getMediaItemsAndUpdateState(_state.value.mediaItemPair.first)
                }
            }
        }

        fun onSelectedCategoryChanged(mediaId: String) {
            if (mediaId == _state.value.mediaItemPair.first) {
                // already loaded.
                return
            }

            getMediaItemsAndUpdateState(mediaId)
        }

        private fun getMediaItemsAndUpdateState(mediaId: String) {
            Log.d(TAG, "getMediaItemsAndUpdateState: $mediaId")
            queryJob?.cancel()

            queryJob =
                viewModelScope.launch {
                    val browser = this@HomeViewModel.browser ?: return@launch

                    val children =
                        browser.getChildren(
                            mediaId,
                            0,
                            Int.MAX_VALUE,
                            null,
                        ).await().value!!

                    val state = this@HomeViewModel._state.value
                    _state.update {
                        state.copy(mediaItemPair = mediaId to children.toList())
                    }
                }
        }

        fun playMusic(mediaItem: MediaItem) {
            val mediaItems = _state.value.mediaItemPair.second

            browser?.run {
                setMediaItems(
                    mediaItems,
                    mediaItems.indexOfFirst { it.mediaId == mediaItem.mediaId },
                    C.TIME_UNSET,
                )
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

data class HomeUiState(
    val categories: List<MediaItem> = emptyList(),
    val mediaItemPair: Pair<String, List<MediaItem>> = ALL_MUSIC_ID to emptyList(),
) {
    val categoryPageContents get() = mediaItemPair.second
}
