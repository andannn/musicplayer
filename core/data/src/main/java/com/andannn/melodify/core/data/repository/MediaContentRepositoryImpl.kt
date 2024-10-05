package com.andannn.melodify.core.data.repository

import com.andannn.melodify.core.data.util.toAppItem
import com.andannn.melodify.core.domain.model.AlbumItemModel
import com.andannn.melodify.core.domain.model.ArtistItemModel
import com.andannn.melodify.core.domain.model.AudioItemModel
import com.andannn.melodify.core.domain.repository.MediaContentRepository
import com.andannn.melodify.core.player.MediaBrowserManager
import com.andannn.melodify.core.player.library.ALBUM_ID
import com.andannn.melodify.core.player.library.ALBUM_PREFIX
import com.andannn.melodify.core.player.library.ALL_MUSIC_ID
import com.andannn.melodify.core.player.library.ARTIST_ID
import com.andannn.melodify.core.player.library.ARTIST_PREFIX
import kotlinx.coroutines.guava.await

internal class MediaContentRepositoryImpl(
    private val mediaBrowserManager: MediaBrowserManager,
) : MediaContentRepository {
    private val mediaBrowser
        get() = mediaBrowserManager.mediaBrowser

    override suspend fun getAllMediaItems() = mediaBrowser.getChildren(
        ALL_MUSIC_ID,
        0,
        Int.MAX_VALUE,
        null,
    ).await().value?.toList()
        ?.map {
            it.toAppItem() as? AudioItemModel ?: throw IllegalStateException("Not a audioItem $it")
        }
        ?: emptyList()

    override suspend fun getAllAlbums() = mediaBrowser.getChildren(
        ALBUM_ID,
        0,
        Int.MAX_VALUE,
        null,
    ).await().value?.toList()
        ?.map {
            it.toAppItem() as? AlbumItemModel ?: throw IllegalStateException("Not a AlbumItem $it")
        }
        ?: emptyList()

    override suspend fun getAllArtist() = mediaBrowser.getChildren(
        ARTIST_ID,
        0,
        Int.MAX_VALUE,
        null,
    ).await().value?.toList()
        ?.map {
            it.toAppItem() as? ArtistItemModel
                ?: throw IllegalStateException("Not a ArtistItem $it")
        }
        ?: emptyList()

    override suspend fun getAudiosOfAlbum(albumId: Long) = mediaBrowser.getChildren(
        ALBUM_PREFIX + albumId,
        0,
        Int.MAX_VALUE,
        null,
    ).await().value?.toList()
        ?.map {
            it.toAppItem() as? AudioItemModel ?: throw IllegalStateException("Not a audioItem $it")
        }
        ?: emptyList()

    override suspend fun getAudiosOfArtist(artistId: Long) = mediaBrowser.getChildren(
        ARTIST_PREFIX + artistId,
        0,
        Int.MAX_VALUE,
        null,
    ).await().value?.toList()
        ?.map {
            it.toAppItem() as? AudioItemModel ?: throw IllegalStateException("Not a audioItem $it")
        }
        ?: emptyList()

    override suspend fun getAlbumByAlbumId(albumId: Long) = mediaBrowser.getItem(
        ALBUM_PREFIX + albumId,
    ).await().value?.let {
        it.toAppItem() as? AlbumItemModel ?: throw IllegalStateException("Invalid $it")
    }

    override suspend fun getArtistByAlbumId(artistId: Long) = mediaBrowser.getItem(
        ARTIST_PREFIX + artistId,
    ).await().value?.let {
        it.toAppItem() as? ArtistItemModel ?: throw IllegalStateException("Invalid $it")
    }
}