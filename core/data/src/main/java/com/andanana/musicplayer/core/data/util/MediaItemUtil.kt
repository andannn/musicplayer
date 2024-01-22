package com.andanana.musicplayer.core.data.util

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaItem.RequestMetadata
import androidx.media3.common.MediaMetadata

fun buildMediaItem(
    title: String,
    mediaId: String,
    isPlayable: Boolean,
    isBrowsable: Boolean,
    mediaType: @MediaMetadata.MediaType Int,
    subtitleConfigurations: List<MediaItem.SubtitleConfiguration> = mutableListOf(),
    album: String? = null,
    artist: String? = null,
    genre: String? = null,
    sourceUri: Uri? = null,
    imageUri: Uri? = null,
    totalTrackCount: Int? = null,
    trackNumber: Int? = null,
): MediaItem {
    val metadata =
        MediaMetadata.Builder()
            .setAlbumTitle(album)
            .setTitle(title)
            .setArtist(artist)
            .setGenre(genre)
            .setIsBrowsable(isBrowsable)
            .setIsPlayable(isPlayable)
            .setArtworkUri(imageUri)
            .setMediaType(mediaType)
            .setTotalTrackCount(totalTrackCount)
            .setTrackNumber(trackNumber)
            .build()

    val requestMetadata = RequestMetadata.Builder()
        .setMediaUri(sourceUri)
        .build()

    return MediaItem.Builder()
        .setMediaId(mediaId)
        .setSubtitleConfigurations(subtitleConfigurations)
        .setMediaMetadata(metadata)
        .setUri(sourceUri)
        .setRequestMetadata(requestMetadata)
        .build()
}

fun MediaItem.isSameDatasource(mediaItem: MediaItem): Boolean {
    return this.mediaId == mediaItem.mediaId
}
