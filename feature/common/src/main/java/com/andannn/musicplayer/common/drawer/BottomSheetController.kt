package com.andannn.musicplayer.common.drawer

import com.andanana.musicplayer.core.domain.model.AlbumItemModel
import com.andanana.musicplayer.core.domain.model.ArtistItemModel
import com.andanana.musicplayer.core.domain.model.AudioItemModel
import com.andanana.musicplayer.core.domain.model.MediaItemModel
import com.andanana.musicplayer.core.domain.repository.MediaControllerRepository
import com.andanana.musicplayer.core.domain.repository.PlayerStateRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BottomSheetModel(
    val source: MediaItemModel,
    val bottomSheet: BottomSheet,
)

interface BottomSheetController {
    val bottomSheetModel: StateFlow<BottomSheetModel?>

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
//        with(browserFuture.await()) {
//            if (!availableCommands.contains(COMMAND_CHANGE_MEDIA_ITEMS)) {
//                Log.d(TAG, "MediaBrowser do not contains COMMAND_CHANGE_MEDIA_ITEMS")
//                return@launch
//            }
//
//            val havePlayingQueue = playerStateRepository.playListQueue.isNotEmpty()
//
//            val items =
//                if (source.mediaMetadata.isBrowsable == true) {
//                    getChildrenById(source.mediaId)
//                } else {
//                    listOf(source)
//                }
//
//            if (havePlayingQueue) {
//                addMediaItems(playerStateRepository.playingIndexInQueue + 1, items)
//            } else {
//                playMediaListFromStart(items)
//            }
//        }
    }
}
