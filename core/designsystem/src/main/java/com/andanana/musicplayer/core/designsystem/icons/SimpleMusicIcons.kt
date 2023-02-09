package com.andanana.musicplayer.core.designsystem.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

object SimpleMusicIcons {
    val Home = Icons.Filled.Home
    val UnselectedHome = Icons.Outlined.Home
    val Settings = Icons.Filled.Settings
    val UnselectedSettings = Icons.Outlined.Settings
    val Library = Icons.Filled.LibraryMusic
    val UnselectedLibrary = Icons.Outlined.LibraryMusic
}

sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
}
