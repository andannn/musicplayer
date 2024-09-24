package com.andannn.melodify.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andannn.melodify.common.drawer.BottomSheetController
import com.andannn.melodify.core.domain.model.MediaItemModel
import com.andannn.melodify.core.domain.model.AudioItemModel
import com.andannn.melodify.core.domain.repository.MediaContentObserverRepository
import com.andannn.melodify.core.domain.repository.MediaControllerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val mediaControllerRepository: MediaControllerRepository,
    private val bottomSheetController: BottomSheetController,
    private val mediaContentObserverRepository: MediaContentObserverRepository,
) : ViewModel() {
    private val _selectedCategoryFlow = MutableStateFlow(MediaCategory.ALBUM)

    private val _contentWithCategoryFlow = _selectedCategoryFlow.flatMapLatest {
        createMediaItemsFlow(it)
    }

    val state = _contentWithCategoryFlow
        .map {
            HomeUiState(
                currentCategory = it.category,
                mediaItems = it.mediaItems.toImmutableList()
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), HomeUiState())

    private fun createMediaItemsFlow(category: MediaCategory): Flow<CategoryWithContents> {
        val contentChangedFlow = with(mediaContentObserverRepository) {
            when (category) {
                MediaCategory.ALL_MUSIC -> getContentChangedEventFlow(allAudioUri)
                MediaCategory.ALBUM -> getContentChangedEventFlow(allAlbumUri)
                MediaCategory.ARTIST -> getContentChangedEventFlow(allArtistUri)
            }
        }

        return contentChangedFlow.mapLatest {
            CategoryWithContents(
                category = category,
                mediaItems = getMediaItemsAndUpdateState(category)
            )
        }
    }

    fun onSelectedCategoryChanged(category: MediaCategory) {
        _selectedCategoryFlow.value = category
    }

    private suspend fun getMediaItemsAndUpdateState(category: MediaCategory): List<MediaItemModel> {
        Log.d(TAG, "getMediaItemsAndUpdateState: $category")

        val a=  when (category) {
            MediaCategory.ALL_MUSIC -> mediaControllerRepository.getAllMediaItems()
            MediaCategory.ALBUM -> mediaControllerRepository.getAllAlbums()
            MediaCategory.ARTIST -> mediaControllerRepository.getAllArtist()
        }
//        Log.d(TAG, "result: $a")
        return  emptyList()
    }

    fun playMusic(mediaItem: AudioItemModel) {
        val mediaItems = state.value.mediaItems.toList() as? List<AudioItemModel>
            ?: error("invalid state")

        mediaControllerRepository.playMediaList(
            mediaItems.toList(),
            mediaItems.indexOf(mediaItem)
        )
    }

    fun onShowMusicItemOption(audioItemModel: AudioItemModel) {
        bottomSheetController.onRequestShowSheet(audioItemModel)
    }
}

private data class CategoryWithContents(
    val category: MediaCategory,
    val mediaItems: List<MediaItemModel>
)

data class HomeUiState(
    val currentCategory: MediaCategory = MediaCategory.ALBUM,
    val mediaItems: ImmutableList<MediaItemModel> = emptyList<MediaItemModel>().toImmutableList()
)