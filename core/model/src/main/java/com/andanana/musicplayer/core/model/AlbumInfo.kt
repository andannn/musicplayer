package com.andanana.musicplayer.core.model

data class AlbumInfo(
    val albumId: Long,
    val title: String,
    val trackCount: Int = 0
)
