package com.andannn.melodify.core.data.model

data class AudioData(
    val id: Long,
    val title: String = "",
    val duration: Int = -1,
    val modifiedDate: Long = -1,
    val size: Int = -1,
    val mimeType: String = "",
    val album: String? = "",
    val albumId: Long? = -1,
    val artist: String? = "",
    val artistId: Long? = -1,
    val cdTrackNumber: Int? = -1,
    val discNumberIndex: Int? = -1,
)
