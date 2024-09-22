package com.andanana.melodify.core.player

import androidx.media3.common.MediaItem

interface MediaLibrarySource {

    fun getLibraryRoot(): MediaItem

    suspend fun getChildren(mediaId: String): List<MediaItem>

    suspend fun getMediaItem(mediaId: String): MediaItem?
}
