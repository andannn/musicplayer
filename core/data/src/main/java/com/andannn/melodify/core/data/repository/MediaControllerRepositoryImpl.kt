package com.andannn.melodify.core.data.repository

import android.util.Log
import androidx.media3.common.C
import com.andannn.melodify.core.domain.model.AlbumItemModel
import com.andannn.melodify.core.domain.model.ArtistItemModel
import com.andannn.melodify.core.domain.model.AudioItemModel
import com.andannn.melodify.core.data.util.toAppItem
import com.andannn.melodify.core.data.util.toExoPlayerMode
import com.andannn.melodify.core.data.util.toMediaItem
import com.andannn.melodify.core.player.library.ALBUM_ID
import com.andannn.melodify.core.player.library.ALBUM_PREFIX
import com.andannn.melodify.core.player.library.ALL_MUSIC_ID
import com.andannn.melodify.core.player.library.ARTIST_ID
import com.andannn.melodify.core.player.library.ARTIST_PREFIX
import com.andannn.melodify.core.domain.model.PlayMode
import com.andannn.melodify.core.domain.repository.MediaControllerRepository
import com.andannn.melodify.core.player.MediaBrowserManager
import kotlinx.coroutines.guava.await
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "MediaControllerRepository"

@Singleton
class MediaControllerRepositoryImpl
@Inject
constructor(
    private val mediaBrowserManager: MediaBrowserManager
) : MediaControllerRepository {

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

    override val duration: Long
        get() = mediaBrowser.duration

    override fun playMediaList(mediaList: List<AudioItemModel>, index: Int) {
        with(mediaBrowser) {
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
        mediaBrowser.seekToNext()
    }

    override fun seekToPrevious() {
        mediaBrowser.seekToPrevious()
    }

    override fun seekMediaItem(mediaItemIndex: Int, positionMs: Long) {
        mediaBrowser.seekTo(mediaItemIndex, positionMs)
    }

    override fun seekToTime(time: Long) {
        mediaBrowser.seekTo(time)
    }

    override fun setPlayMode(mode: PlayMode) {
        mediaBrowser.repeatMode = mode.toExoPlayerMode()
    }

    override fun setShuffleModeEnabled(enable: Boolean) {
        mediaBrowser.shuffleModeEnabled = enable
    }

    override fun play() {
        mediaBrowser.play()
    }

    override fun pause() {
        mediaBrowser.pause()
    }

    override fun addMediaItems(index: Int, mediaItems: List<AudioItemModel>) {
        Log.d(TAG, "addMediaItems: index $index, mediaItems $mediaItems")
        mediaBrowser.addMediaItems(
            /* index = */ index,
            /* mediaItems = */ mediaItems.map { it.toMediaItem(generateUniqueId = true) }
        )
    }

    override fun moveMediaItem(from: Int, to: Int) {
        mediaBrowser.moveMediaItem(from, to)
    }

    override fun removeMediaItem(index: Int) {
        mediaBrowser.removeMediaItem(index)
    }
}