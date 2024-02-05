package com.andanana.musicplayer.core.designsystem.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.PlaylistAdd
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.PlaylistAdd
import androidx.compose.material.icons.rounded.Share
import androidx.compose.ui.graphics.vector.ImageVector

object SimpleMusicIcons {
    val Home = Icons.Filled.Home
    val UnselectedHome = Icons.Outlined.Home
    val Settings = Icons.Filled.Settings
    val UnselectedSettings = Icons.Outlined.Settings
    val Library = Icons.Filled.LibraryMusic
    val UnselectedLibrary = Icons.Outlined.LibraryMusic
    val Share = Icon.ImageVectorIcon(Icons.Rounded.Share)
    val AddFavorite = Icon.ImageVectorIcon(Icons.Rounded.Favorite)
    val AddPlayList = Icon.ImageVectorIcon(Icons.AutoMirrored.Rounded.PlaylistAdd)
    val PlayNext = Icon.ImageVectorIcon(Icons.Rounded.PlayCircle)
    val Information = Icon.ImageVectorIcon(Icons.Rounded.Info)
    val Delete = Icon.ImageVectorIcon(Icons.Rounded.Delete)
}

sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
}
