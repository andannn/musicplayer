package com.andanana.musicplayer.core.model

import android.net.Uri

data class MusicInfo(
    val contentUri: Uri,
    val title: String = "",
    val duration: Int = 0,
    val modifiedDate: Int = 0,
    val size: Int = 0,
    val mimeType: String = "",
    val absolutePath: String = "",
    val album: String = "",
    val artist: String = "",
    val albumUri: Uri = Uri.parse("")
)
