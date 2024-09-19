package com.andanana.musicplayer.core.data.repository

import androidx.media3.common.C
import androidx.media3.session.MediaBrowser
import com.andanana.musicplayer.core.data.model.AlbumItem
import com.andanana.musicplayer.core.data.model.AudioItem
import com.andanana.musicplayer.core.data.model.toAppItem
import com.andanana.musicplayer.core.data.model.toMediaItem
import com.andanana.musicplayer.core.data.util.getOrNull
import com.andanana.musicplayer.core.model.ALBUM_PREFIX
import com.andanana.musicplayer.core.model.ALL_MUSIC_ID
import com.andanana.musicplayer.core.model.ARTIST_PREFIX
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
        it.toAppItem() as? AudioItem ?: throw IllegalStateException("Not a audioItem $it")
    }

    override suspend fun getAudiosOfAlbum(albumId: Long) = getMediaBrowser().getChildren(
        ALBUM_PREFIX + albumId,
        0,
        Int.MAX_VALUE,
        null,
    ).await().value!!.toList().map {
        it.toAppItem() as? AudioItem ?: throw IllegalStateException("Not a audioItem $it")
    }

    override suspend fun getAudiosOfArtist(artistId: Long) = getMediaBrowser().getChildren(
        ARTIST_PREFIX + artistId,
        0,
        Int.MAX_VALUE,
        null,
    ).await().value!!.toList().map {
        it.toAppItem() as? AudioItem ?: throw IllegalStateException("Not a audioItem $it")
    }

    override suspend fun getAudioCategories(): List<String> {
        return emptyList()
    }

    override suspend fun getAlbumByAlbumId(albumId: Long) = getMediaBrowser().getItem(
        ALBUM_PREFIX + albumId,
    ).await().value?.let {
        it.toAppItem() as? AlbumItem ?: throw IllegalStateException("Invalid $it")
    }

    override fun playMediaList(mediaList: List<AudioItem>, index: Int, isShuffle: Boolean) {
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

    private suspend fun getMediaBrowser(): MediaBrowser {
        return browserFuture.await()
    }
}