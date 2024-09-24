package com.andannn.melodify.core.data.repository

import android.util.Log
import androidx.media3.common.C
import androidx.media3.session.MediaBrowser
import com.andannn.melodify.core.domain.model.AlbumItemModel
import com.andannn.melodify.core.domain.model.ArtistItemModel
import com.andannn.melodify.core.domain.model.AudioItemModel
import com.andannn.melodify.core.data.util.getOrNull
import com.andannn.melodify.core.data.util.toAppItem
import com.andannn.melodify.core.data.util.toExoPlayerMode
import com.andannn.melodify.core.data.util.toMediaItem
import com.andannn.melodify.core.domain.model.ALBUM_ID
import com.andannn.melodify.core.domain.model.ALBUM_PREFIX
import com.andannn.melodify.core.domain.model.ALL_MUSIC_ID
import com.andannn.melodify.core.domain.model.ARTIST_ID
import com.andannn.melodify.core.domain.model.ARTIST_PREFIX
import com.andannn.melodify.core.domain.model.PlayMode
import com.andannn.melodify.core.domain.repository.MediaControllerRepository
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.guava.await
import javax.inject.Inject

private const val TAG = "MediaControllerRepository"

class MediaControllerRepositoryImpl
@Inject
constructor(
    private val browserFuture: ListenableFuture<MediaBrowser>
) : MediaControllerRepository {
    override suspend fun getAllMediaItems() = getMediaBrowser().getChildren(
        ALL_MUSIC_ID,
        0,
        Int.MAX_VALUE,
        null,
    ).await().value?.toList()
        ?.map {
            it.toAppItem() as? AudioItemModel ?: throw IllegalStateException("Not a audioItem $it")
        }
        ?: emptyList()

    override suspend fun getAllAlbums() = getMediaBrowser().getChildren(
        ALBUM_ID,
        0,
        Int.MAX_VALUE,
        null,
    ).await().value?.toList()
        ?.map {
            it.toAppItem() as? AlbumItemModel ?: throw IllegalStateException("Not a AlbumItem $it")
        }
        ?: emptyList()

    override suspend fun getAllArtist() = getMediaBrowser().getChildren(
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

    override suspend fun getAudiosOfAlbum(albumId: Long) = getMediaBrowser().getChildren(
        ALBUM_PREFIX + albumId,
        0,
        Int.MAX_VALUE,
        null,
    ).await().value?.toList()
        ?.map {
            it.toAppItem() as? AudioItemModel ?: throw IllegalStateException("Not a audioItem $it")
        }
        ?: emptyList()

    override suspend fun getAudiosOfArtist(artistId: Long) = getMediaBrowser().getChildren(
        ARTIST_PREFIX + artistId,
        0,
        Int.MAX_VALUE,
        null,
    ).await().value?.toList()
        ?.map {
            it.toAppItem() as? AudioItemModel ?: throw IllegalStateException("Not a audioItem $it")
        }
        ?: emptyList()

    override suspend fun getAlbumByAlbumId(albumId: Long) = getMediaBrowser().getItem(
        ALBUM_PREFIX + albumId,
    ).await().value?.let {
        it.toAppItem() as? AlbumItemModel ?: throw IllegalStateException("Invalid $it")
    }

    override suspend fun getArtistByAlbumId(artistId: Long) = getMediaBrowser().getItem(
        ARTIST_PREFIX + artistId,
    ).await().value?.let {
        it.toAppItem() as? ArtistItemModel ?: throw IllegalStateException("Invalid $it")
    }

    override val duration: Long?
        get() = getMediaBrowserOrNull()?.duration

    override fun playMediaList(mediaList: List<AudioItemModel>, index: Int) {
        val browser = browserFuture.getOrNull() ?: error("MediaBrowser is not ready")
        with(browser) {
            setMediaItems(
                mediaList.map { it.toMediaItem(generateUniqueId = true) },
                index,
                C.TIME_UNSET,
            )
            prepare()
            play()
        }
    }

    override fun seekToNext() {
        getMediaBrowserOrNull()?.seekToNext()
    }

    override fun seekToPrevious() {
        getMediaBrowserOrNull()?.seekToPrevious()
    }

    override fun seekMediaItem(mediaItemIndex: Int, positionMs: Long) {
        getMediaBrowserOrNull()?.seekTo(mediaItemIndex, positionMs)
    }

    override fun seekToTime(time: Long) {
        getMediaBrowserOrNull()?.seekTo(time)
    }

    override fun setPlayMode(mode: PlayMode) {
        getMediaBrowserOrNull()?.repeatMode = mode.toExoPlayerMode()
    }

    override fun setShuffleModeEnabled(enable: Boolean) {
        getMediaBrowserOrNull()?.shuffleModeEnabled = enable
    }

    override fun play() {
        getMediaBrowserOrNull()?.play()
    }

    override fun pause() {
        getMediaBrowserOrNull()?.pause()
    }

    override fun addMediaItems(index: Int, mediaItems: List<AudioItemModel>) {
        Log.d(TAG, "addMediaItems: index $index, mediaItems $mediaItems")
        getMediaBrowserOrNull()?.addMediaItems(
            /* index = */ index,
            /* mediaItems = */ mediaItems.map { it.toMediaItem(generateUniqueId = true) }
        )
    }

    override fun moveMediaItem(from: Int, to: Int) {
        getMediaBrowserOrNull()?.moveMediaItem(from, to)
    }

    override fun removeMediaItem(index: Int) {
        getMediaBrowserOrNull()?.removeMediaItem(index)
    }

    private suspend fun getMediaBrowser(): MediaBrowser {
        return browserFuture.await()
    }

    private fun getMediaBrowserOrNull(): MediaBrowser? {
        return browserFuture.getOrNull()
    }
}