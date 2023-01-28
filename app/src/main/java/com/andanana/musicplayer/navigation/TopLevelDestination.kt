package com.andanana.musicplayer.navigation

import com.andanana.musicplayer.R
import com.andanana.musicplayer.core.designsystem.icons.Icon
import com.andanana.musicplayer.core.designsystem.icons.SimpleMusicIcons
import com.andanana.musicplayer.feature.home.R as homeR
import com.andanana.musicplayer.feature.library.R as libraryR

enum class TopLevelDestination(
    val selectedIcon: Icon,
    val unSelectedIcon: Icon,
    val iconTextId: Int,
    val titleTextId: Int
) {
    HOME(
        selectedIcon = Icon.ImageVectorIcon(SimpleMusicIcons.Home),
        unSelectedIcon = Icon.ImageVectorIcon(SimpleMusicIcons.UnselectedHome),
        iconTextId = homeR.string.home,
        titleTextId = R.string.app_name
    ),
    LIBRARY(
        selectedIcon = Icon.ImageVectorIcon(SimpleMusicIcons.Library),
        unSelectedIcon = Icon.ImageVectorIcon(SimpleMusicIcons.UnselectedLibrary),
        iconTextId = libraryR.string.title,
        titleTextId = libraryR.string.title
    ),
    SETTING(
        selectedIcon = Icon.ImageVectorIcon(SimpleMusicIcons.Settings),
        unSelectedIcon = Icon.ImageVectorIcon(SimpleMusicIcons.UnselectedSettings),
        iconTextId = R.string.app_name,
        titleTextId = R.string.app_name
    ),
}
