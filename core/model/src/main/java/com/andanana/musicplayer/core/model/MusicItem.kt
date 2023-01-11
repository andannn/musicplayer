package com.andanana.musicplayer.core.model

import android.net.Uri

data class MusicItem(
    val contentUri: Uri,
    val artist: String = "",
    val duration: Int = 0,
    val absolutePath: String = "",
    val album: String = ""
)
