package com.andanana.musicplayer.core.data.model

import android.net.Uri

data class ArtistModel(
    val artistId: Long,
    val artistUri: Uri = Uri.EMPTY,
    val name: String,
    val trackCount: Int = 0,
)
