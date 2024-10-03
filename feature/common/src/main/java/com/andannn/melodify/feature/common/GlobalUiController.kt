package com.andannn.melodify.feature.common

import com.andannn.melodify.feature.common.drawer.SheetOptionItem
import com.andannn.melodify.core.data.util.uri
import com.andannn.melodify.core.domain.model.AlbumItemModel
import com.andannn.melodify.core.domain.model.ArtistItemModel
import com.andannn.melodify.core.domain.model.AudioItemModel
import com.andannn.melodify.core.domain.model.MediaItemModel
import com.andannn.melodify.core.domain.repository.MediaControllerRepository
import com.andannn.melodify.core.domain.repository.PlayerStateRepository
import com.andannn.melodify.feature.common.drawer.SheetModel
import com.andannn.melodify.feature.common.drawer.SleepTimerOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface UiEvent {
    data class OnOptionClick(
        val sheet: SheetModel.MediaOptionSheet,
        val clickedItem: SheetOptionItem?,
    ) : UiEvent

    data class OnClickTimerOption(
        val option: SleepTimerOption?,
    ) : UiEvent
}

interface DeleteMediaItemEventProvider {
    val deleteMediaItemEventFlow: SharedFlow<List<String>>
}

interface BottomSheetStateProvider {
    val bottomSheetModel: StateFlow<SheetModel?>
}

interface GlobalUiController : BottomSheetStateProvider, DeleteMediaItemEventProvider {
    fun showBottomSheet(sheet: SheetModel)

    /**
     * Handle the dismiss request from the bottom sheet.
     */
    fun CoroutineScope.onDismissRequest(event: UiEvent)
}

internal class GlobalUiControllerImpl(
    private val mediaControllerRepository: MediaControllerRepository,
    private val playerStateRepository: PlayerStateRepository,
) : GlobalUiController {
    override val bottomSheetModel: StateFlow<SheetModel?>
        get() = _bottomSheetModelFlow.asStateFlow()

    override val deleteMediaItemEventFlow: SharedFlow<List<String>>
        get() = _deleteMediaItemEventFlow

    private val _bottomSheetModelFlow = MutableStateFlow<SheetModel?>(null)
    private val _deleteMediaItemEventFlow = MutableSharedFlow<List<String>>(1)

    override fun showBottomSheet(sheet: SheetModel) {
        _bottomSheetModelFlow.value = sheet
    }

    override fun CoroutineScope.onDismissRequest(event: UiEvent) {
        when (event) {
            is UiEvent.OnClickTimerOption -> {

            }
            is UiEvent.OnOptionClick -> {
                    event.clickedItem?.let {
                        when (it) {
                            SheetOptionItem.PLAY_NEXT -> onPlayNextClick(event.sheet.source)
                            SheetOptionItem.DELETE -> onDeleteMediaItem(event.sheet.source)
                            SheetOptionItem.ADD_TO_QUEUE -> onAddToQueue(event.sheet.source)
                            SheetOptionItem.SLEEP_TIMER -> TODO()
                        }
                    }

            }
        }

        _bottomSheetModelFlow.value = null
    }

    private fun CoroutineScope.onDeleteMediaItem(source: MediaItemModel) = launch {
        val items = when (source) {
            is AlbumItemModel -> {
                mediaControllerRepository.getAudiosOfAlbum(source.id)
            }

            is ArtistItemModel -> {
                mediaControllerRepository.getAudiosOfArtist(source.id)
            }

            is AudioItemModel -> {
                listOf(source)
            }
        }
        val uris = items.map { it.uri.toString() }

        _deleteMediaItemEventFlow.tryEmit(uris)
    }

    private fun CoroutineScope.onPlayNextClick(source: MediaItemModel) = launch {
        val items = getAudios(source)
        val havePlayingQueue = playerStateRepository.playListQueue.isNotEmpty()
        if (havePlayingQueue) {
            mediaControllerRepository.addMediaItems(
                index = playerStateRepository.playingIndexInQueue + 1,
                mediaItems = items
            )
        } else {
            mediaControllerRepository.playMediaList(items, 0)
        }
    }

    private fun CoroutineScope.onAddToQueue(source: MediaItemModel) = launch {
        val items = getAudios(source)
        val playListQueue = playerStateRepository.playListQueue
        if (playListQueue.isNotEmpty()) {
            mediaControllerRepository.addMediaItems(
                index = playListQueue.size,
                mediaItems = items
            )
        } else {
            mediaControllerRepository.playMediaList(items, 0)
        }
    }

    private suspend fun getAudios(source: MediaItemModel): List<AudioItemModel> {
        return when (source) {
            is AlbumItemModel -> {
                mediaControllerRepository.getAudiosOfAlbum(source.id)
            }

            is ArtistItemModel -> {
                mediaControllerRepository.getAudiosOfArtist(source.id)
            }

            is AudioItemModel -> {
                listOf(source)
            }
        }
    }
}
