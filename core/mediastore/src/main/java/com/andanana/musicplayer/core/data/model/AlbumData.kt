package com.andanana.musicplayer.core.data.model

import android.net.Uri

data class AlbumData(
    val albumId: Long,
    val albumUri: Uri,
    val title: String,
    val trackCount: Int = 0
)