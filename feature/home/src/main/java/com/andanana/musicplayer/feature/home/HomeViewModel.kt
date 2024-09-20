package com.andanana.musicplayer.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.domain.model.MediaItemModel
import com.andanana.musicplayer.core.domain.model.AudioItemModel
import com.andanana.musicplayer.core.domain.repository.MediaControllerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val mediaControllerRepository: MediaControllerRepository,
//    private val contentChangeFlowProvider: ContentChangeFlowProvider,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    private var queryJob: Job? = null

    init {
        viewModelScope.launch {
            getMediaItemsAndUpdateState(MediaCategory.ALL_MUSIC)
//                contentChangeFlowProvider.audioChangedEventFlow.collect { _ ->
//                    getMediaItemsAndUpdateState(_state.value.mediaItemPair.first)
//                }
        }
    }

    fun onSelectedCategoryChanged(category: MediaCategory) {
//            if (mediaId == _state.value.mediaItemPair.first) {
//                // already loaded.
//                return
//            }

        getMediaItemsAndUpdateState(category)
    }

    private fun getMediaItemsAndUpdateState(category: MediaCategory) {
        Log.d(TAG, "getMediaItemsAndUpdateState: $category")
        queryJob?.cancel()

        queryJob =
            viewModelScope.launch {
                val items = when (category) {
                    MediaCategory.ALL_MUSIC -> mediaControllerRepository.getAllMediaItems()
                    MediaCategory.ALBUM -> mediaControllerRepository.getAllAlbums()
                    MediaCategory.ARTIST -> mediaControllerRepository.getAllArtist()
                }
                val state = this@HomeViewModel._state.value
                _state.update {
                    state.copy(
                        currentCategory = category,
                        mediaItems = items.toImmutableList()
                    )
                }
            }
    }

    fun playMusic(mediaItem: AudioItemModel) {
//        val mediaItems = _state.value.mediaItemPair.second

//        mediaControllerRepository.playMediaList(mediaItems, 0, false)
    }
}

data class HomeUiState(
    val currentCategory: MediaCategory = MediaCategory.ALL_MUSIC,
    val mediaItems: ImmutableList<MediaItemModel> = emptyList<MediaItemModel>().toImmutableList()
)