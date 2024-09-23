package com.andannn.melodify.common.drawer

import com.andannn.melodify.core.designsystem.icons.SmpIcon
import com.andannn.melodify.core.designsystem.icons.SimpleMusicIcons

enum class SheetItem(
    val smpIcon: SmpIcon,
    val text: String,
) {
    ADD_TO_FAVORITE(
        smpIcon = SimpleMusicIcons.AddFavorite,
        text = "Save to Favorite",
    ),
    PLAY_NEXT(
        smpIcon = SimpleMusicIcons.PlayNext,
        text = "Play next",
    ),
    ADD_TO_PLAY_LIST(
        smpIcon = SimpleMusicIcons.AddPlayList,
        text = "Save to PlayList",
    ),
    SHARE(
        smpIcon = SimpleMusicIcons.Share,
        text = "Share",
    ),
    INFORMATION(
        smpIcon = SimpleMusicIcons.Information,
        text = "Information",
    ),
    DELETE(
        smpIcon = SimpleMusicIcons.Delete,
        text = "Delete",
    ),
}

sealed class BottomSheet(
    val itemList: List<SheetItem>,
) {
    data object MusicBottomSheet : BottomSheet(
        listOf(
            SheetItem.PLAY_NEXT,
        ),
    )

    data object AlbumBottomSheet : BottomSheet(
        listOf(
            SheetItem.PLAY_NEXT,
        ),
    )

    data object ArtistBottomSheet : BottomSheet(
        listOf(
            SheetItem.PLAY_NEXT,
        ),
    )

    data object PlayListBottomSheet : BottomSheet(
        listOf(
            SheetItem.PLAY_NEXT,
            SheetItem.INFORMATION,
            SheetItem.DELETE,
        ),
    )
}
