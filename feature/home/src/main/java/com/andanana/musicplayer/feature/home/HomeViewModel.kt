package com.andanana.musicplayer.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaBrowser
import com.andanana.musicplayer.core.data.ContentChangeFlowProvider
import com.andanana.musicplayer.core.data.model.ALL_MUSIC_ID
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
        private var currentMediaCategoryId = ALL_MUSIC_ID

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
                        // parentId =
                        root.value!!.mediaId,
                        // page =
                        0,
                        // pageSize =
                        Int.MAX_VALUE,
                        // params =
                        null,
                    ).await().value!!.toList()

                _state.update {
                    HomeUiState(
                        categories = categories,
                    )
                }

                contentChangeFlowProvider.audioChangedEventFlow.collect { isSyncing ->
                    getMediaItemsAndUpdateState(currentMediaCategoryId)
                }
            }
        }

        fun onSelectedCategoryChanged(mediaId: String) {
            currentMediaCategoryId = mediaId

            if (state.value.categoryToMediaItemsMap.containsKey(mediaId)) {
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
                            // parentId =
                            mediaId,
                            // page =
                            0,
                            // pageSize =
                            Int.MAX_VALUE,
                            // params =
                            null,
                        ).await().value!!

                    val state = this@HomeViewModel._state.value
                    _state.update {
                        state.copy(
                            categoryToMediaItemsMap =
                                state.categoryToMediaItemsMap.toMutableMap().apply {
                                    put(mediaId, children)
                                },
                        )
                    }
                }
        }

        fun playMusic(mediaItem: MediaItem) {
            val state = this._state.value

            browser?.run {
//            setMediaItems(
//                state.currentMusicItems,
//                /* startIndex= */
//                state.currentMusicItems.indexOfFirst { it.mediaId == mediaItem.mediaId },
//                /* startPositionMs= */
//                C.TIME_UNSET
//            )
                shuffleModeEnabled = false
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
    val categoryToMediaItemsMap: Map<String, List<MediaItem>> = emptyMap(),
) {
    val categoryPageContents
        get() =
            categories.map {
                categoryToMediaItemsMap.getOrDefault(it.mediaId, defaultValue = emptyList())
            }
}
