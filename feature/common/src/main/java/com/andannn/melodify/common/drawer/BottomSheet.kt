package com.andannn.melodify.common.drawer

import com.andannn.melodify.common.R
import com.andannn.melodify.core.designsystem.icons.SmpIcon
import com.andannn.melodify.core.designsystem.icons.SimpleMusicIcons

enum class SheetItem(
    val smpIcon: SmpIcon,
    val text: Int,
) {
    PLAY_NEXT(
        smpIcon = SimpleMusicIcons.PlayNext,
        text = R.string.play_next,
    ),
    DELETE(
        smpIcon = SimpleMusicIcons.Delete,
        text = R.string.delete,
    ),
    ADD_TO_QUEUE(
        smpIcon = SimpleMusicIcons.Delete,
        text = R.string.add_to_queue,
    ),
}

sealed class BottomSheet(
    val itemList: List<SheetItem>,
) {
    data object MusicBottomSheet : BottomSheet(
        listOf(
            SheetItem.ADD_TO_QUEUE,
            SheetItem.PLAY_NEXT,
            SheetItem.DELETE,
        ),
    )

    data object AlbumBottomSheet : BottomSheet(
        listOf(
            SheetItem.ADD_TO_QUEUE,
            SheetItem.PLAY_NEXT,
            SheetItem.DELETE,
        ),
    )

    data object ArtistBottomSheet : BottomSheet(
        listOf(
            SheetItem.ADD_TO_QUEUE,
            SheetItem.PLAY_NEXT,
            SheetItem.DELETE,
        ),
    )

    data object PlayListBottomSheet : BottomSheet(
        listOf(
            SheetItem.ADD_TO_QUEUE,
            SheetItem.PLAY_NEXT,
            SheetItem.DELETE,
        ),
    )
}
