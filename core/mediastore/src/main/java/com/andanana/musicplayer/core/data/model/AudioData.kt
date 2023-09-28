package com.andanana.musicplayer.core.data.model

import android.net.Uri

data class AudioData(
    val contentUri: Uri,
    val title: String = "",
    val duration: Int = -1,
    val modifiedDate: Long = -1,
    val size: Int = -1,
    val mimeType: String = "",
    val album: String = "",
    val albumId: Int = -1,
    val artist: String = "",
    val artistId: Int = -1,
    val albumUri: String = "",
    val cdTrackNumber: Int = -1,
    val discNumberIndex: Int = -1
)