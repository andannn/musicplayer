package com.andannn.musicplayer.common.drawer

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.COMMAND_CHANGE_MEDIA_ITEMS
import androidx.media3.session.MediaBrowser
import com.andanana.musicplayer.core.data.repository.PlayerStateRepository
import com.andanana.musicplayer.core.data.util.getChildrenById
import com.andanana.musicplayer.core.data.util.playMediaListFromStart
import com.andanana.musicplayer.core.model.MediaSourceType
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch

private const val TAG = "BottomSheetController"

data class BottomSheetModel(
    val source: MediaItem,
    val bottomSheet: BottomSheet,
)

data class MediaOptionResult(
    val source: MediaItem,
    val item: SheetItem,
)

interface BottomSheetController {
    val bottomSheetModel: StateFlow<BottomSheetModel?>

    fun onRequestShowSheet(mediaItem: MediaItem)

    fun onDismissRequest(item: SheetItem?)
}

class BottomSheetControllerImpl(
    private val coroutineScope: CoroutineScope,
    private val browserFuture: ListenableFuture<MediaBrowser>,
    private val playerMoRepository: PlayerStateRepository,
) : BottomSheetController, CoroutineScope by coroutineScope {
    override val bottomSheetModel: StateFlow<BottomSheetModel?>
        get() = _bottomSheetModelFlow.asStateFlow()

    private val _bottomSheetModelFlow = MutableStateFlow<BottomSheetModel?>(null)

    override fun onRequestShowSheet(mediaItem: MediaItem) {
        _bottomSheetModelFlow.value =
            BottomSheetModel(
                source = mediaItem,
                bottomSheet = buildDrawer(mediaItem),
            )
    }

    private fun buildDrawer(mediaItem: MediaItem): BottomSheet {
        val source =
            MediaSourceType.getMediaSourceType(mediaItem.mediaId)
                ?: error("no need to show drawer for ${mediaItem.mediaId}")
        return when (source) {
            MediaSourceType.MUSIC -> BottomSheet.MusicBottomSheet
            MediaSourceType.ARTIST -> BottomSheet.ArtistBottomSheet
            MediaSourceType.ALBUM -> BottomSheet.AlbumBottomSheet
        }
    }

    override fun onDismissRequest(item: SheetItem?) {
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

    private fun onPlayNextClick(source: MediaItem) {
        launch {
            with(browserFuture.await()) {
                if (!availableCommands.contains(COMMAND_CHANGE_MEDIA_ITEMS)) {
                    Log.d(TAG, "MediaBrowser do not contains COMMAND_CHANGE_MEDIA_ITEMS")
                    return@launch
                }

                val havePlayingQueue = playerMoRepository.playListQueueStateFlow.value.isNotEmpty()

                val items =
                    if (source.mediaMetadata.isBrowsable == true) {
                        getChildrenById(source.mediaId)
                    } else {
                        listOf(source)
                    }

                if (havePlayingQueue) {
                    addMediaItems(playerMoRepository.playingIndexInQueue + 1, items)
                } else {
                    playMediaListFromStart(items)
                }
            }
        }
    }
}
