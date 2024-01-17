package com.andanana.musicplayer.core.data.repository

import androidx.media3.common.MediaItem

interface MusicRepository {

    fun getLibraryRoot(): MediaItem

    suspend fun getChildren(mediaId: String): List<MediaItem>

    suspend fun getMediaItem(mediaId: String): MediaItem?
}
