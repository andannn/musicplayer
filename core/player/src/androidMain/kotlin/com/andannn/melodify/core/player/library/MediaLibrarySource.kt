package com.andannn.melodify.core.player.library

import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.andannn.melodify.core.player.library.mediastore.MediaStoreSource
import com.andannn.melodify.core.player.library.mediastore.model.AlbumData
import com.andannn.melodify.core.player.library.mediastore.model.ArtistData
import com.andannn.melodify.core.player.library.mediastore.model.AudioData
import com.andannn.melodify.core.player.util.buildMediaItem

interface MediaLibrarySource {

    fun getLibraryRoot(): MediaItem

    suspend fun getChildren(mediaId: String): List<MediaItem>

    suspend fun getMediaItem(mediaId: String): MediaItem?
}

private const val TAG = "MusicRepositoryImpl"

class MediaLibrarySourceImpl(
    private val mediaStoreSource: MediaStoreSource
) : MediaLibrarySource {

    override fun getLibraryRoot(): MediaItem {
        return buildMediaItem(
            title = "Root Folder",
            mediaId = ROOT_ID,
            isPlayable = false,
            isBrowsable = true,
            mediaType = MediaMetadata.MEDIA_TYPE_FOLDER_MIXED,
        )
    }

    override suspend fun getChildren(mediaId: String): List<MediaItem> {
        Log.d(TAG, "getChildren: mediaId $mediaId")
        return if (mediaId == ROOT_ID) {
            LibraryRootCategory.entries.mapNotNull { category ->
                getMediaItem(category.mediaId)
            }
        } else if (mediaId == LibraryRootCategory.ALL_MUSIC.mediaId) {
            mediaStoreSource.getAllMusicData().map(::buildMusicMediaItem)
        } else if (mediaId == LibraryRootCategory.ALBUM.mediaId) {
            mediaStoreSource.getAllAlbumData().map(::buildAlbumMediaItem)
        } else if (mediaId == LibraryRootCategory.ARTIST.mediaId) {
            mediaStoreSource.getAllArtistData().map(::buildArtistMediaItem)
        } else if (mediaId == LibraryRootCategory.MINE_PLAYLIST.mediaId) {
// TODO:
            emptyList()
        } else if (LibraryRootCategory.getMatchedChildTypeAndId(mediaId) != null) {
            val (category, id) = LibraryRootCategory.getMatchedChildTypeAndId(mediaId)!!
            when (category) {
                LibraryRootCategory.ALBUM -> {
                    mediaStoreSource.getAudioInAlbum(id).map { music ->
                        buildMusicMediaItem(music)
                    }
                }

                LibraryRootCategory.ARTIST -> {
                    mediaStoreSource.getAudioOfArtist(id).map { music ->
                        buildMusicMediaItem(music)
                    }
                }

                LibraryRootCategory.MINE_PLAYLIST -> TODO()
                else -> emptyList()
            }
        } else {
            emptyList()
        }
    }

    override suspend fun getMediaItem(mediaId: String): MediaItem? {
        Log.d(TAG, "getMediaItem: mediaId $mediaId")
        return if (mediaId == ROOT_ID) {
            buildMediaItem(
                title = "ROOT",
                mediaId = mediaId,
                isPlayable = false,
                isBrowsable = true,
                mediaType = MediaMetadata.MEDIA_TYPE_FOLDER_MIXED,
            )
        } else if (mediaId in LibraryRootCategory.entries.map { it.mediaId }) {
            buildMediaItem(
                title = mediaId,
                mediaId = mediaId,
                isPlayable = false,
                isBrowsable = true,
                mediaType = MediaMetadata.MEDIA_TYPE_FOLDER_MIXED,
            )
        } else if (LibraryRootCategory.getMatchedChildTypeAndId(mediaId) != null) {
            val (category, id) = LibraryRootCategory.getMatchedChildTypeAndId(mediaId)!!
            when (category) {
                LibraryRootCategory.ALBUM -> {
                    mediaStoreSource.getAlbumById(id)?.let {
                        buildAlbumMediaItem(it)
                    }
                }

                LibraryRootCategory.ARTIST -> {
                    mediaStoreSource.getArtistById(id)?.let {
                        buildArtistMediaItem(it)
                    }
                }

                LibraryRootCategory.MINE_PLAYLIST -> TODO()
                else -> null
            }
        } else {
            null
        }
    }
}

private fun buildAlbumMediaItem(album: AlbumData): MediaItem =
    buildMediaItem(
        title = album.title,
        mediaId = LibraryRootCategory.ALBUM.childrenPrefix + album.albumId,
        imageUri =
        Uri.withAppendedPath(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            album.albumId.toString(),
        ),
        totalTrackCount = album.trackCount,
        isPlayable = false,
        isBrowsable = true,
        mediaType = MediaMetadata.MEDIA_TYPE_FOLDER_MIXED,
    )

private fun buildArtistMediaItem(artist: ArtistData): MediaItem =
    buildMediaItem(
        title = artist.name,
        mediaId = LibraryRootCategory.ARTIST.childrenPrefix + artist.artistId,
        imageUri =
        Uri.withAppendedPath(
            MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
            artist.artistId.toString(),
        ),
        totalTrackCount = artist.trackCount,
        isPlayable = false,
        isBrowsable = true,
        mediaType = MediaMetadata.MEDIA_TYPE_FOLDER_MIXED,
    )

private fun buildMusicMediaItem(music: AudioData): MediaItem =
    buildMediaItem(
        title = music.title,
        sourceUri =
        Uri.withAppendedPath(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            music.id.toString(),
        ),
        mediaId = PLAYABLE_MEDIA_ITEM_PREFIX + music.id,
        imageUri =
        Uri.withAppendedPath(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            music.albumId.toString(),
        ),
        trackNumber = music.cdTrackNumber,
        album = music.album,
        artist = music.artist,
        isPlayable = true,
        isBrowsable = false,
        mediaType = MediaMetadata.MEDIA_TYPE_MUSIC,
    )
