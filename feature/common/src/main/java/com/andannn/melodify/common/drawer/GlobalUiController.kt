package com.andannn.melodify.common.drawer

import com.andannn.melodify.core.data.util.uri
import com.andannn.melodify.core.domain.model.AlbumItemModel
import com.andannn.melodify.core.domain.model.ArtistItemModel
import com.andannn.melodify.core.domain.model.AudioItemModel
import com.andannn.melodify.core.domain.model.MediaItemModel
import com.andannn.melodify.core.domain.repository.MediaControllerRepository
import com.andannn.melodify.core.domain.repository.PlayerStateRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface SheetModel {
    abstract class MediaOptionSheet(
        open val source: MediaItemModel,
        open val options: List<SheetOptionItem>,
    ) : SheetModel {
        companion object {
            fun fromMediaModel(item: MediaItemModel): SheetModel {
                return when (item) {
                    is AlbumItemModel -> AlbumOptionSheet(item)
                    is ArtistItemModel -> ArtistOptionSheet(item)
                    is AudioItemModel -> AudioOptionSheet(item)
                }
            }
        }
    }

    data class AudioOptionSheet(override val source: AudioItemModel) : MediaOptionSheet(
        source = source,
        options = listOf(
            SheetOptionItem.ADD_TO_QUEUE,
            SheetOptionItem.PLAY_NEXT,
            SheetOptionItem.DELETE,
        ),
    )

    data class PlayerOptionSheet(override val source: AudioItemModel) : MediaOptionSheet(
        source = source,
        options = listOf(
            SheetOptionItem.ADD_TO_QUEUE,
            SheetOptionItem.PLAY_NEXT,
            SheetOptionItem.SLEEP_TIMER,
        ),
    )

    data class AlbumOptionSheet(override val source: AlbumItemModel) : MediaOptionSheet(
        source = source,
        options = listOf(
            SheetOptionItem.ADD_TO_QUEUE,
            SheetOptionItem.PLAY_NEXT,
            SheetOptionItem.DELETE,
        ),
    )

    data class ArtistOptionSheet(override val source: ArtistItemModel) : MediaOptionSheet(
        source = source,
        options = listOf(
            SheetOptionItem.ADD_TO_QUEUE,
            SheetOptionItem.PLAY_NEXT,
            SheetOptionItem.DELETE,
        ),
    )

    data object TimerSheet : SheetModel

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
     *
     * @param sheet The bottom sheet that is being dismissed.
     * @param item The item that is clicked in the bottom sheet, which can be null.
     */
    fun CoroutineScope.onDismissRequest(sheet: SheetModel, item: SheetOptionItem?)
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

    override fun CoroutineScope.onDismissRequest(sheet: SheetModel, item: SheetOptionItem?) {
        when (sheet) {
            is SheetModel.MediaOptionSheet -> {
                if (item != null) {
                    when (item) {
                        SheetOptionItem.PLAY_NEXT -> onPlayNextClick(sheet.source)
                        SheetOptionItem.DELETE -> onDeleteMediaItem(sheet.source)
                        SheetOptionItem.ADD_TO_QUEUE -> onAddToQueue(sheet.source)
                        SheetOptionItem.SLEEP_TIMER -> TODO()
                    }
                }
            }

            SheetModel.TimerSheet -> TODO()
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
