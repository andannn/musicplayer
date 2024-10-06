package com.andannn.melodify.core.player.library.mediastore.model

import android.net.Uri

data class ArtistData(
    val artistId: Long,
    val artistCoverUri: Uri,
    val name: String,
    val trackCount: Int = 0,
)
