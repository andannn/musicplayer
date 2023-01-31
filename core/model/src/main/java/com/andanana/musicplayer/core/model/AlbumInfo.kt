package com.andanana.musicplayer.core.model

import android.net.Uri

data class AlbumInfo(
    val albumId: Long,
    val albumUri: Uri,
    val title: String,
    val trackCount: Int = 0
)
