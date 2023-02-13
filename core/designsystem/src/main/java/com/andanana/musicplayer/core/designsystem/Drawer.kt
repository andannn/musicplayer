package com.andanana.musicplayer.core.designsystem

import com.andanana.musicplayer.core.designsystem.icons.Icon
import com.andanana.musicplayer.core.designsystem.icons.SimpleMusicIcons

enum class DrawerItem(
    val icon: Icon,
    val text: String
) {
    ADD_TO_FAVORITE(
        icon = SimpleMusicIcons.AddFavorite,
        text = "Save to Favorite"
    ),
    ADD_TO_PLAY_LIST(
        icon = SimpleMusicIcons.AddPlayList,
        text = "Save to PlayList"
    ),
    Share(
        icon = SimpleMusicIcons.Share,
        text = "Share"
    ),
}

sealed class Drawer(
    val itemList: List<DrawerItem>
) {
    object MusicDrawer : Drawer(
        listOf(
            DrawerItem.ADD_TO_FAVORITE,
            DrawerItem.ADD_TO_PLAY_LIST,
            DrawerItem.Share,
        )
    )
}
