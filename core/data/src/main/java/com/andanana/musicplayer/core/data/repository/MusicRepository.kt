package com.andanana.musicplayer.core.data.repository

import androidx.media3.common.MediaItem
import com.andanana.musicplayer.core.data.model.AlbumModel
import com.andanana.musicplayer.core.data.model.ArtistModel
import com.andanana.musicplayer.core.data.model.MusicModel
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    suspend fun sync()

    fun getAllMusics(): Flow<List<MusicModel>>

    fun getAllArtists(): Flow<List<ArtistModel>>

    fun getAllAlbums(): Flow<List<AlbumModel>>

    fun getMusicsInAlbum(albumId: Long): Flow<List<MusicModel>>

    fun getMusicsInArtist(artistId: Long): Flow<List<MusicModel>>

    suspend fun getAlbumById(albumId: Long): AlbumModel

    suspend fun getArtistById(artistId: Long): ArtistModel

    fun getLibraryRoot(): MediaItem

    suspend fun getChildren(mediaId: String): List<MediaItem>
}