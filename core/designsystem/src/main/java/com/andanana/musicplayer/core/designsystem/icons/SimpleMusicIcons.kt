package com.andanana.musicplayer.core.designsystem.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

object SimpleMusicIcons {
    val Settings = Icons.Outlined.Settings
    val Music = Icons.Outlined.PlayArrow
    val Playlists = Icons.Outlined.List
}

sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
}