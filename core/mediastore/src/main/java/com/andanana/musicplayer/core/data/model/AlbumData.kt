package com.andanana.musicplayer.core.data.model

data class AlbumData constructor(
    val albumId: Long,
    val title: String,
    val trackCount: Int = 0
)