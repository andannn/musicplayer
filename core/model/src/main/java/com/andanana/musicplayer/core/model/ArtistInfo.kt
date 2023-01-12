package com.andanana.musicplayer.core.model

data class ArtistInfo(
    val artistId: Long,
    val name: String,
    val trackCount: Int = 0
)
