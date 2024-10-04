package com.andannn.melodify.core.domain.repository

import com.andannn.melodify.core.domain.model.AlbumItemModel
import com.andannn.melodify.core.domain.model.ArtistItemModel
import com.andannn.melodify.core.domain.model.AudioItemModel
import com.andannn.melodify.core.domain.model.PlayMode
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration

interface MediaControllerRepository {
    suspend fun getAllMediaItems(): List<AudioItemModel>

    suspend fun getAllAlbums(): List<AlbumItemModel>

    suspend fun getAllArtist(): List<ArtistItemModel>

    suspend fun getAudiosOfAlbum(albumId: Long): List<AudioItemModel>

    suspend fun getAudiosOfArtist(artistId: Long): List<AudioItemModel>

    suspend fun getAlbumByAlbumId(albumId: Long): AlbumItemModel?

    suspend fun getArtistByAlbumId(artistId: Long): ArtistItemModel?

    val duration: Long?

    fun playMediaList(mediaList: List<AudioItemModel>, index: Int)

    fun seekToNext()

    fun seekToPrevious()

    fun seekMediaItem(mediaItemIndex: Int, positionMs: Long = 0)

    fun seekToTime(time: Long)

    fun setPlayMode(mode: PlayMode)

    fun setShuffleModeEnabled(enable: Boolean)

    fun play()

    fun pause()

    fun addMediaItems(index: Int, mediaItems: List<AudioItemModel>)

    fun moveMediaItem(from: Int, to: Int)

    fun removeMediaItem(index: Int)

    fun isCounting(): Boolean

    fun observeRemainTime(): Flow<Duration>

    fun startSleepTimer(duration: Duration)

    fun cancelSleepTimer()
}