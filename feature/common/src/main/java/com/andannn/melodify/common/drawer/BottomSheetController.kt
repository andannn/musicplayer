package com.andannn.melodify.common.drawer

import com.andannn.melodify.core.domain.model.AlbumItemModel
import com.andannn.melodify.core.domain.model.ArtistItemModel
import com.andannn.melodify.core.domain.model.AudioItemModel
import com.andannn.melodify.core.domain.model.MediaItemModel
import com.andannn.melodify.core.domain.repository.MediaControllerRepository
import com.andannn.melodify.core.domain.repository.PlayerStateRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BottomSheetModel(
    val source: MediaItemModel,
    val bottomSheet: BottomSheet,
)

interface BottomSheetStateProvider {
    val bottomSheetModel: StateFlow<BottomSheetModel?>
}

interface BottomSheetController: BottomSheetStateProvider {
    fun onRequestShowSheet(mediaItem: MediaItemModel)

    fun CoroutineScope.onDismissRequest(item: SheetItem?)
}

internal class BottomSheetControllerImpl(
    private val mediaControllerRepository: MediaControllerRepository,
    private val playerStateRepository: PlayerStateRepository,
) : BottomSheetController {
    override val bottomSheetModel: StateFlow<BottomSheetModel?>
        get() = _bottomSheetModelFlow.asStateFlow()

    private val _bottomSheetModelFlow = MutableStateFlow<BottomSheetModel?>(null)

    override fun onRequestShowSheet(mediaItem: MediaItemModel) {
        _bottomSheetModelFlow.value =
            BottomSheetModel(
                source = mediaItem,
                bottomSheet = buildDrawer(mediaItem),
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
                SheetItem.ADD_TO_FAVORITE -> TODO()
                SheetItem.PLAY_NEXT -> onPlayNextClick(_bottomSheetModelFlow.value!!.source)
                SheetItem.ADD_TO_PLAY_LIST -> TODO()
                SheetItem.SHARE -> TODO()
                SheetItem.INFORMATION -> TODO()
                SheetItem.DELETE -> TODO()
            }
        }

        _bottomSheetModelFlow.value = null
    }

    private fun CoroutineScope.onPlayNextClick(source: MediaItemModel) = launch {

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
}
