package com.andannn.melodify.core.player.mediastore.model

data class AlbumData(
    val albumId: Long,
    val title: String,
    val trackCount: Int = 0,
)
