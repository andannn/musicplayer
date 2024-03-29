package com.andannn.musicplayer.common.drawer

import androidx.media3.common.MediaItem
import com.andanana.musicplayer.core.model.MediaSourceType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class BottomSheetModel(
    val source: MediaItem,
    val bottomSheet: BottomSheet,
)

data class MediaOptionResult(
    val source: MediaItem,
    val item: SheetItem,
)

interface BottomSheetEventSink {
    fun onRequestShowSheet(mediaItem: MediaItem)

    fun onDismissRequest(item: SheetItem?)
}

interface BottomSheetController : BottomSheetEventSink {
    val bottomSheetModel: StateFlow<BottomSheetModel?>

    fun observeMediaOptionResult(): Flow<BottomSheetModel>
}

class BottomSheetControllerImpl : BottomSheetController {
    private var lastRequestedMediaItem: MediaItem? = null

    override val bottomSheetModel: StateFlow<BottomSheetModel?>
        get() = _bottomSheetModelFlow.asStateFlow()

    private val _bottomSheetModelFlow =
        MutableStateFlow<BottomSheetModel?>(null)

    override fun onRequestShowSheet(mediaItem: MediaItem) {
        lastRequestedMediaItem = mediaItem

        _bottomSheetModelFlow.value =
            BottomSheetModel(
                source = mediaItem,
                bottomSheet = buildDrawer(mediaItem),
            )
    }

    private fun buildDrawer(mediaItem: MediaItem): BottomSheet {
        val source =
            MediaSourceType.getMediaSourceType(mediaItem.mediaId!!)
                ?: error("no need to show drawer for ${mediaItem.mediaId}")
        return when (source) {
            MediaSourceType.MUSIC -> BottomSheet.MusicBottomSheet
            MediaSourceType.ARTIST -> BottomSheet.ArtistBottomSheet
            MediaSourceType.ALBUM -> BottomSheet.AlbumBottomSheet
        }
    }

    override fun onDismissRequest(item: SheetItem?) {
        _bottomSheetModelFlow.value = null
    }

    override fun observeMediaOptionResult(): Flow<BottomSheetModel> {
        TODO("Not yet implemented")
    }
}
