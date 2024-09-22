package com.andannn.melodify.core.domain.repository

import com.andannn.melodify.core.domain.model.AlbumItemModel
import com.andannn.melodify.core.domain.model.ArtistItemModel
import com.andannn.melodify.core.domain.model.AudioItemModel
import com.andannn.melodify.core.domain.model.PlayMode

interface MediaControllerRepository {
    suspend fun getAllMediaItems(): List<AudioItemModel>

    suspend fun getAllAlbums(): List<AlbumItemModel>

    suspend fun getAllArtist(): List<ArtistItemModel>

    suspend fun getAudiosOfAlbum(albumId: Long): List<AudioItemModel>

    suspend fun getAudiosOfArtist(artistId: Long): List<AudioItemModel>

    suspend fun getAlbumByAlbumId(albumId: Long): AlbumItemModel?

    suspend fun getArtistByAlbumId(artistId: Long): ArtistItemModel?

    val duration: Long?

    fun playMediaList(mediaList: List<AudioItemModel>, index: Int, isShuffle: Boolean)

    fun seekToNext()

    fun seekToPrevious()

    fun seekToTime(time: Long)

    fun setPlayMode(mode: PlayMode)

    fun setShuffleModeEnabled(enable: Boolean)

    fun play()

    fun pause()
}