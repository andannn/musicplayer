package com.andanana.musicplayer.navigation

import com.andanana.musicplayer.R
import com.andanana.musicplayer.core.designsystem.icons.Icon
import com.andanana.musicplayer.core.designsystem.icons.SimpleMusicIcons
import com.andanana.musicplayer.feature.musiclist.R as musicListR

enum class TopLevelDestination(
    val selectedIcon: Icon,
    val unSelectedIcon: Icon,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    MUSIC_LIST(
        selectedIcon = Icon.ImageVectorIcon(SimpleMusicIcons.Music),
        unSelectedIcon = Icon.ImageVectorIcon(SimpleMusicIcons.Music),
        iconTextId = musicListR.string.music_list,
        titleTextId = R.string.app_name,
    ),
    PLAY_LIST(
        selectedIcon = Icon.ImageVectorIcon(SimpleMusicIcons.Playlists),
        unSelectedIcon = Icon.ImageVectorIcon(SimpleMusicIcons.Playlists),
        iconTextId = musicListR.string.music_list,
        titleTextId = R.string.app_name,
    )
}