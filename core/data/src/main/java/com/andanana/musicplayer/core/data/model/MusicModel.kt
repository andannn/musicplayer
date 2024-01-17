package com.andanana.musicplayer.core.data.model

import android.net.Uri

data class MusicModel(
    val id: Long,
    val contentUri: Uri = Uri.EMPTY,
    val title: String = "",
    val duration: Int = 0,
    val modifiedDate: Long = 0,
    val size: Int = 0,
    val mimeType: String = "",
    val absolutePath: String = "",
    val album: String = "",
    val artist: String = "",
    val albumUri: Uri = Uri.EMPTY,
    val cdTrackNumber: Int = 0,
    val discNumberIndex: Int = 0,
)
