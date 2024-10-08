package com.andannn.melodify.core.data

import com.andannn.melodify.core.data.model.AlbumItemModel
import com.andannn.melodify.core.data.model.ArtistItemModel
import com.andannn.melodify.core.data.model.AudioItemModel
import com.andannn.melodify.core.data.model.GenreItemModel
import kotlinx.coroutines.flow.Flow

interface MediaContentRepository {
    fun getAllMediaItemsFlow(): Flow<List<AudioItemModel>>

    fun getAllAlbumsFlow(): Flow<List<AlbumItemModel>>

    fun getAllArtistFlow(): Flow<List<ArtistItemModel>>

    fun getAllGenreFlow(): Flow<List<GenreItemModel>>

    fun getAudiosOfAlbumFlow(albumId: Long): Flow<List<AudioItemModel>>

    suspend fun getAudiosOfAlbum(albumId: Long): List<AudioItemModel>

    fun getAudiosOfArtistFlow(artistId: Long): Flow<List<AudioItemModel>>

    suspend fun getAudiosOfArtist(artistId: Long): List<AudioItemModel>

    fun getAudiosOfGenreFlow(genreId: Long): Flow<List<AudioItemModel>>

    suspend fun getAudiosOfGenre(genreId: Long): List<AudioItemModel>

    fun getAlbumByAlbumIdFlow(albumId: Long): Flow<AlbumItemModel?>

    fun getArtistByArtistIdFlow(artistId: Long): Flow<ArtistItemModel?>

    fun getGenreByGenreIdFlow(genreId: Long): Flow<GenreItemModel?>

    suspend fun getAlbumByAlbumId(albumId: Long): AlbumItemModel?

    suspend fun getArtistByArtistId(artistId: Long): ArtistItemModel?

    suspend fun getGenreByGenreId(genreId: Long): GenreItemModel?
}