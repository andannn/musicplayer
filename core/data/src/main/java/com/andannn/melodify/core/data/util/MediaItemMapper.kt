package com.andannn.melodify.core.data.util

import android.net.Uri
import android.provider.MediaStore
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.andannn.melodify.core.domain.model.AlbumItemModel
import com.andannn.melodify.core.domain.model.ArtistItemModel
import com.andannn.melodify.core.domain.model.AudioItemModel
import com.andannn.melodify.core.domain.model.LibraryRootCategory
import com.andannn.melodify.core.domain.model.MediaItemModel
import com.andannn.melodify.core.domain.model.PLAYABLE_MEDIA_ITEM_PREFIX
import com.andannn.melodify.core.player.buildMediaItem


fun MediaItem.toAppItem(): MediaItemModel = when {
    mediaId.contains(PLAYABLE_MEDIA_ITEM_PREFIX) -> AudioItemModel(
        id = mediaId.substringAfter(PLAYABLE_MEDIA_ITEM_PREFIX).toLong(),
        name = mediaMetadata.title.toString(),
        modifiedDate = 0,
        album = mediaMetadata.albumTitle.toString(),
        albumId = 0,
        artist = mediaMetadata.artist.toString(),
        artistId = 0,
        cdTrackNumber = mediaMetadata.trackNumber ?: 0,
        discNumberIndex = 0,
        artWorkUri = mediaMetadata.artworkUri.toString()
    )

    mediaId.contains(LibraryRootCategory.ALBUM.childrenPrefix) -> AlbumItemModel(
        id = mediaId.substringAfter(LibraryRootCategory.ALBUM.childrenPrefix).toLong(),
        name = mediaMetadata.title.toString(),
        trackCount = mediaMetadata.totalTrackCount ?: 0,
        artWorkUri = mediaMetadata.artworkUri.toString()
    )

    mediaId.contains(LibraryRootCategory.ARTIST.childrenPrefix) -> ArtistItemModel(
        id = mediaId.substringAfter(LibraryRootCategory.ARTIST.childrenPrefix).toLong(),
        name = mediaMetadata.title.toString(),
        trackCount = mediaMetadata.totalTrackCount ?: 0,
        artWorkUri = mediaMetadata.artworkUri.toString()
    )

    else -> error("Not a AppMediaItem $this")
}

fun AudioItemModel.toMediaItem(): MediaItem {
    return buildMediaItem(
        title = name,
        sourceUri =
        Uri.withAppendedPath(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            id.toString(),
        ),
        mediaId = PLAYABLE_MEDIA_ITEM_PREFIX + id,
        imageUri = Uri.parse(artWorkUri),
        trackNumber = cdTrackNumber,
        album = album,
        artist = artist,
        isPlayable = true,
        isBrowsable = false,
        mediaType = MediaMetadata.MEDIA_TYPE_MUSIC,
    )
}
