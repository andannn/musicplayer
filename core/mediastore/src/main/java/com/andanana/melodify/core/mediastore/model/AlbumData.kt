package com.andanana.melodify.core.mediastore.model

data class AlbumData(
    val albumId: Long,
    val title: String,
    val trackCount: Int = 0,
)
