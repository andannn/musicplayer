package com.andanana.musicplayer.core.data.model

import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Audio
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.andanana.musicplayer.core.model.LibraryRootCategory
import com.andanana.musicplayer.core.model.PLAYABLE_MEDIA_ITEM_PREFIX
import com.andanana.musicplayer.core.player.buildMediaItem


sealed interface AppMediaItem {
    val id: Long
    val name: String
}

data class AudioItem(
    override val id: Long,
    override val name: String,
    val modifiedDate: Long,
    val album: String,
    val albumId: Long,
    val artist: String,
    val artistId: Long,
    val cdTrackNumber: Int,
    val discNumberIndex: Int,
) : AppMediaItem

data class AlbumItem(
    override val id: Long,
    override val name: String,
    val trackCount: Int,
    val artWorkUri: String,
) : AppMediaItem {
    companion object {
        val DEFAULT = AlbumItem(0, "", 0, "")
    }
}

data class ArtistItem(
    override val id: Long,
    override val name: String,
    val trackCount: Int,
    val artistCoverUri: String,
) : AppMediaItem

fun MediaItem.toAppItem(): AppMediaItem = when {
    mediaId.contains(PLAYABLE_MEDIA_ITEM_PREFIX) -> AudioItem(
        id = mediaId.substringAfter(PLAYABLE_MEDIA_ITEM_PREFIX).toLong(),
        name = mediaMetadata.title.toString(),
        modifiedDate = 0,
        album = mediaMetadata.albumTitle.toString(),
        albumId = 0,
        artist = mediaMetadata.artist.toString(),
        artistId = 0,
        cdTrackNumber = mediaMetadata.trackNumber ?: 0,
        discNumberIndex = 0
    )

    mediaId.contains(LibraryRootCategory.ALBUM.childrenPrefix) -> AlbumItem(
        id = mediaId.substringAfter(LibraryRootCategory.ALBUM.childrenPrefix).toLong(),
        name = mediaMetadata.title.toString(),
        trackCount = mediaMetadata.totalTrackCount ?: 0,
        artWorkUri = mediaMetadata.artworkUri.toString()
    )

    mediaId.contains(LibraryRootCategory.ARTIST.childrenPrefix) -> ArtistItem(
        id = mediaId.substringAfter(LibraryRootCategory.ARTIST.childrenPrefix).toLong(),
        name = mediaMetadata.title.toString(),
        trackCount = mediaMetadata.totalTrackCount ?: 0,
        artistCoverUri = mediaMetadata.artworkUri.toString()
    )

    else -> error("Not a AppMediaItem $this")
}

fun AudioItem.toMediaItem(): MediaItem {
    return buildMediaItem(
        title = name,
        sourceUri =
        Uri.withAppendedPath(
            Audio.Media.EXTERNAL_CONTENT_URI,
            id.toString(),
        ),
        mediaId = PLAYABLE_MEDIA_ITEM_PREFIX + id,
        imageUri =
        Uri.withAppendedPath(
            Audio.Albums.EXTERNAL_CONTENT_URI,
            albumId.toString(),
        ),
        trackNumber = cdTrackNumber,
        album = album,
        artist = artist,
        isPlayable = true,
        isBrowsable = false,
        mediaType = MediaMetadata.MEDIA_TYPE_MUSIC,
    )
}
