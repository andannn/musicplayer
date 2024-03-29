package com.andannn.musicplayer.common.drawer

import com.andanana.musicplayer.core.designsystem.icons.Icon
import com.andanana.musicplayer.core.designsystem.icons.SimpleMusicIcons

enum class DrawerItem(
    val icon: Icon,
    val text: String,
) {
    ADD_TO_FAVORITE(
        icon = SimpleMusicIcons.AddFavorite,
        text = "Save to Favorite",
    ),
    PLAY_NEXT(
        icon = SimpleMusicIcons.PlayNext,
        text = "Play next",
    ),
    ADD_TO_PLAY_LIST(
        icon = SimpleMusicIcons.AddPlayList,
        text = "Save to PlayList",
    ),
    SHARE(
        icon = SimpleMusicIcons.Share,
        text = "Share",
    ),
    INFORMATION(
        icon = SimpleMusicIcons.Information,
        text = "Information",
    ),
    DELETE(
        icon = SimpleMusicIcons.Delete,
        text = "Delete",
    ),
}

sealed class Drawer(
    val itemList: List<DrawerItem>,
) {
    data object MusicDrawer : Drawer(
        listOf(
            DrawerItem.ADD_TO_FAVORITE,
            DrawerItem.ADD_TO_PLAY_LIST,
            DrawerItem.PLAY_NEXT,
            DrawerItem.INFORMATION,
        ),
    )

    data object AlbumDrawer : Drawer(
        listOf(
            DrawerItem.PLAY_NEXT,
            DrawerItem.INFORMATION,
        ),
    )

    data object ArtistDrawer : Drawer(
        listOf(
            DrawerItem.PLAY_NEXT,
            DrawerItem.INFORMATION,
        ),
    )

    data object PlayListDrawer : Drawer(
        listOf(
            DrawerItem.PLAY_NEXT,
            DrawerItem.INFORMATION,
            DrawerItem.DELETE,
        ),
    )
}
