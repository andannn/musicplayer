package com.andanana.musicplayer.core.data.model

import android.net.Uri

data class AlbumModel(
    val albumId: Long,
    val albumUri: Uri = Uri.EMPTY,
    val title: String,
    val trackCount: Int = 0,
)
