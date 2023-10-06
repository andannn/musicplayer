package com.andanana.musicplayer.feature.home

import android.app.Application
import android.content.ComponentName
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionToken
import com.andanana.musicplayer.core.data.model.ALL_MUSIC_ID
import com.andanana.musicplayer.core.data.model.LibraryRootCategory
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
) : ViewModel() {
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

    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            // wait browser build complete.
            val browser = browserFuture.await()

            val root = browser.getLibraryRoot(null).await()
            val categories = browser.getChildren(
                root.value!!.mediaId,
                /* page= */ 0,
                /* pageSize= */ Int.MAX_VALUE,
                /* params= */ null
            ).await().value!!.toList()

            val allMusicItems = browser.getChildren(
                LibraryRootCategory.ALL_MUSIC.mediaId,
                /* page= */ 0,
                /* pageSize= */ Int.MAX_VALUE,
                /* params= */ null
            ).await().value!!.toList()

            _state.update {
                HomeUiState.Ready(
                    categories = categories,
                    categoryToMediaItemsMap = mapOf(ALL_MUSIC_ID to allMusicItems)
                )
            }
            Log.d(TAG, "${_state.value}")
        }
    }

    fun onSelectedCategoryChanged(mediaId: String) {
        val state = this._state.value

        if (state is HomeUiState.Loading) return

        val readyState = state as HomeUiState.Ready
        val items = readyState.categoryToMediaItemsMap.getOrDefault(
            mediaId,
            null
        )
        if (items != null) {
            // already loaded.
            _state.update {
                readyState.copy(selectedCategory = mediaId)
            }
        } else {
            viewModelScope.launch {
                val browser = this@HomeViewModel.browser ?: return@launch

                val children = browser.getChildren(
                    mediaId,
                    /* page= */ 0,
                    /* pageSize= */ Int.MAX_VALUE,
                    /* params= */ null
                ).await().value!!

                _state.update {
                    readyState.copy(
                        selectedCategory = mediaId,
                        categoryToMediaItemsMap = readyState.categoryToMediaItemsMap
                            .toMutableMap()
                            .apply {
                                put(mediaId, children)
                            }
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        MediaBrowser.releaseFuture(browserFuture)
        browser?.release()
    }
}

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Ready(
        val selectedCategory: String = ALL_MUSIC_ID,
        val categories: List<MediaItem> = emptyList(),
        val categoryToMediaItemsMap: Map<String, List<MediaItem>> = emptyMap(),
    ) : HomeUiState {
        val currentMusicItems
            get() = categoryToMediaItemsMap.getOrDefault(
                selectedCategory,
                defaultValue = emptyList()
            )
    }
}
