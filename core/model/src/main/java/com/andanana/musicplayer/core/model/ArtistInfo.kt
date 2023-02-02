package com.andanana.musicplayer.core.model

import android.net.Uri

data class ArtistInfo(
    val artistId: Long,
    val artistCoverUri: Uri,
    val name: String,
    val trackCount: Int = 0
)
