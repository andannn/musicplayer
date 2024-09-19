package com.andanana.musicplayer.core.data.repository

import androidx.media3.common.C
import androidx.media3.session.MediaBrowser
import com.andanana.musicplayer.core.domain.model.AlbumItemModel
import com.andanana.musicplayer.core.domain.model.ArtistItemModel
import com.andanana.musicplayer.core.domain.model.AudioItemModel
import com.andanana.musicplayer.core.data.util.getOrNull
import com.andanana.musicplayer.core.data.util.toAppItem
import com.andanana.musicplayer.core.data.util.toExoPlayerMode
import com.andanana.musicplayer.core.data.util.toMediaItem
import com.andanana.musicplayer.core.domain.model.ALBUM_ID
import com.andanana.musicplayer.core.domain.model.ALBUM_PREFIX
import com.andanana.musicplayer.core.domain.model.ALL_MUSIC_ID
import com.andanana.musicplayer.core.domain.model.ARTIST_ID
import com.andanana.musicplayer.core.domain.model.ARTIST_PREFIX
import com.andanana.musicplayer.core.domain.model.PlayMode
import com.andanana.musicplayer.core.domain.repository.MediaControllerRepository
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.guava.await
import javax.inject.Inject

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
    ).await().value!!.toList().map {
        it.toAppItem() as? AudioItemModel ?: throw IllegalStateException("Not a audioItem $it")
    }

    override suspend fun getAllAlbums() = getMediaBrowser().getChildren(
        ALBUM_ID,
        0,
        Int.MAX_VALUE,
        null,
    ).await().value!!.toList().map {
        it.toAppItem() as? AlbumItemModel ?: throw IllegalStateException("Not a AlbumItem $it")
    }

    override suspend fun getAllArtist() = getMediaBrowser().getChildren(
        ARTIST_ID,
        0,
        Int.MAX_VALUE,
        null,
    ).await().value!!.toList().map {
        it.toAppItem() as? ArtistItemModel ?: throw IllegalStateException("Not a ArtistItem $it")
    }

    override suspend fun getAudiosOfAlbum(albumId: Long) = getMediaBrowser().getChildren(
        ALBUM_PREFIX + albumId,
        0,
        Int.MAX_VALUE,
        null,
    ).await().value!!.toList().map {
        it.toAppItem() as? AudioItemModel ?: throw IllegalStateException("Not a audioItem $it")
    }

    override suspend fun getAudiosOfArtist(artistId: Long) = getMediaBrowser().getChildren(
        ARTIST_PREFIX + artistId,
        0,
        Int.MAX_VALUE,
        null,
    ).await().value!!.toList().map {
        it.toAppItem() as? AudioItemModel ?: throw IllegalStateException("Not a audioItem $it")
    }

    override suspend fun getAlbumByAlbumId(albumId: Long) = getMediaBrowser().getItem(
        ALBUM_PREFIX + albumId,
    ).await().value?.let {
        it.toAppItem() as? AlbumItemModel ?: throw IllegalStateException("Invalid $it")
    }

    override val duration: Long?
        get() = getMediaBrowserOrNull()?.duration

    override fun playMediaList(mediaList: List<AudioItemModel>, index: Int, isShuffle: Boolean) {
        val browser = browserFuture.getOrNull() ?: error("MediaBrowser is not ready")
        with(browser) {
            setMediaItems(
                mediaList.map { it.toMediaItem() },
                index,
                C.TIME_UNSET,
            )
            shuffleModeEnabled = isShuffle
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

    private suspend fun getMediaBrowser(): MediaBrowser {
        return browserFuture.await()
    }
    private  fun getMediaBrowserOrNull(): MediaBrowser? {
        return browserFuture.getOrNull()
    }
}