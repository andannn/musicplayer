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

data class BottomSheetModel(
    val source: MediaItemModel,
    val bottomSheet: BottomSheet,
)

interface DeleteMediaItemEventProvider {
    val deleteMediaItemEventFlow: SharedFlow<List<String>>
}

interface BottomSheetStateProvider {
    val bottomSheetModel: StateFlow<BottomSheetModel?>
}

interface BottomSheetController : BottomSheetStateProvider, DeleteMediaItemEventProvider {
    fun onRequestShowSheet(mediaItem: MediaItemModel, sheet: BottomSheet? = null)

    fun CoroutineScope.onDismissRequest(item: SheetItem?)
}

internal class BottomSheetControllerImpl(
    private val mediaControllerRepository: MediaControllerRepository,
    private val playerStateRepository: PlayerStateRepository,
) : BottomSheetController {
    override val bottomSheetModel: StateFlow<BottomSheetModel?>
        get() = _bottomSheetModelFlow.asStateFlow()

    override val deleteMediaItemEventFlow: SharedFlow<List<String>>
        get() = _deleteMediaItemEventFlow

    private val _bottomSheetModelFlow = MutableStateFlow<BottomSheetModel?>(null)
    private val _deleteMediaItemEventFlow = MutableSharedFlow<List<String>>(1)

    override fun onRequestShowSheet(mediaItem: MediaItemModel, sheet: BottomSheet?) {
        _bottomSheetModelFlow.value =
            BottomSheetModel(
                source = mediaItem,
                bottomSheet = sheet ?: buildDrawer(mediaItem),
            )
    }

    private fun buildDrawer(mediaItem: MediaItemModel): BottomSheet {

        return when (mediaItem) {
            is AlbumItemModel -> BottomSheet.AlbumBottomSheet
            is ArtistItemModel -> BottomSheet.ArtistBottomSheet
            is AudioItemModel -> BottomSheet.MusicBottomSheet
        }
    }

    override fun CoroutineScope.onDismissRequest(item: SheetItem?) {
        if (item != null) {
            when (item) {
                SheetItem.PLAY_NEXT -> onPlayNextClick(_bottomSheetModelFlow.value!!.source)
                SheetItem.DELETE -> onDeleteMediaItem(_bottomSheetModelFlow.value!!.source)
                SheetItem.ADD_TO_QUEUE -> onAddToQueue(_bottomSheetModelFlow.value!!.source)
                SheetItem.SLEEP_TIMER -> TODO()
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
