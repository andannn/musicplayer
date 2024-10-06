package com.andannn.melodify.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andannn.melodify.feature.common.GlobalUiController
import com.andannn.melodify.core.data.model.MediaItemModel
import com.andannn.melodify.core.data.model.AudioItemModel
import com.andannn.melodify.core.data.model.MediaPreviewMode
import com.andannn.melodify.core.data.MediaContentObserverRepository
import com.andannn.melodify.core.data.MediaContentRepository
import com.andannn.melodify.core.data.MediaControllerRepository
import com.andannn.melodify.core.data.UserPreferenceRepository
import com.andannn.melodify.feature.common.drawer.SheetModel
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"

sealed interface HomeUiEvent {
    data class OnSelectedCategoryChanged(val category: MediaCategory) : HomeUiEvent
    data class OnPlayMusic(val mediaItem: AudioItemModel) : HomeUiEvent
    data class OnShowItemOption(val audioItemModel: MediaItemModel) : HomeUiEvent
    data object OnTogglePreviewMode : HomeUiEvent
}

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val mediaControllerRepository: MediaControllerRepository,
    private val globalUiController: GlobalUiController,
    private val mediaContentRepository: MediaContentRepository,
    private val mediaContentObserverRepository: MediaContentObserverRepository,
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {
    private val _selectedCategoryFlow = MutableStateFlow(MediaCategory.ALBUM)

    private val _contentWithCategoryFlow = _selectedCategoryFlow.flatMapLatest {
        createMediaItemsFlow(it)
    }

    private val _previewModeFlow = userPreferenceRepository.previewMode

    val state = combine(
        _contentWithCategoryFlow,
        _previewModeFlow,
    ) { contentWithCategory, previewMode ->
        HomeUiState(
            currentCategory = contentWithCategory.category,
            mediaItems = contentWithCategory.mediaItems.toImmutableList(),
            previewMode = previewMode
        )
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), HomeUiState())

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnSelectedCategoryChanged -> onSelectedCategoryChanged(event.category)
            is HomeUiEvent.OnPlayMusic -> playMusic(event.mediaItem)
            is HomeUiEvent.OnShowItemOption -> onShowMusicItemOption(event.audioItemModel)
            is HomeUiEvent.OnTogglePreviewMode -> onTogglePreviewMode()
        }
    }

    private fun onTogglePreviewMode() {
        viewModelScope.launch {
            userPreferenceRepository.setPreviewMode(state.value.previewMode.next())
        }
    }

    private fun onSelectedCategoryChanged(category: MediaCategory) {
        _selectedCategoryFlow.value = category
    }

    private fun playMusic(mediaItem: AudioItemModel) {
        val mediaItems = state.value.mediaItems.toList() as? List<AudioItemModel>
            ?: error("invalid state")

        mediaControllerRepository.playMediaList(
            mediaItems.toList(),
            mediaItems.indexOf(mediaItem)
        )
    }

    private fun onShowMusicItemOption(mediaItemModel: MediaItemModel) {
        viewModelScope.launch {
            globalUiController.updateBottomSheet(
                SheetModel.MediaOptionSheet.fromMediaModel(mediaItemModel)
            )
        }
    }

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

    private suspend fun getMediaItemsAndUpdateState(category: MediaCategory): List<MediaItemModel> {
        Napier.d(tag = TAG) { "getMediaItemsAndUpdateState: $category" }

        return when (category) {
            MediaCategory.ALL_MUSIC -> mediaContentRepository.getAllMediaItems()
            MediaCategory.ALBUM -> mediaContentRepository.getAllAlbums()
            MediaCategory.ARTIST -> mediaContentRepository.getAllArtist()
        }
    }
}

private data class CategoryWithContents(
    val category: MediaCategory,
    val mediaItems: List<MediaItemModel>
)

data class HomeUiState(
    val currentCategory: MediaCategory = MediaCategory.ALBUM,
    val mediaItems: ImmutableList<MediaItemModel> = emptyList<MediaItemModel>().toImmutableList(),
    val previewMode: MediaPreviewMode = MediaPreviewMode.GRID_PREVIEW
)