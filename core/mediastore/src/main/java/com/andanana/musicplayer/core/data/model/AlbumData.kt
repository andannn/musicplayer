package com.andanana.musicplayer.core.data.model

data class AlbumData(
    val albumId: Long,
    val title: String,
    val trackCount: Int = 0,
)
