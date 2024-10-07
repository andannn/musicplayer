package com.andannn.melodify.core.data

import com.andannn.melodify.core.data.model.AlbumItemModel
import com.andannn.melodify.core.data.model.ArtistItemModel
import com.andannn.melodify.core.data.model.AudioItemModel
import com.andannn.melodify.core.data.model.GenreItemModel

interface MediaContentRepository {
    suspend fun getAllMediaItems(): List<AudioItemModel>

    suspend fun getAllAlbums(): List<AlbumItemModel>

    suspend fun getAllArtist(): List<ArtistItemModel>

    suspend fun getAllGenre(): List<GenreItemModel>

    suspend fun getAudiosOfAlbum(albumId: Long): List<AudioItemModel>

    suspend fun getAudiosOfArtist(artistId: Long): List<AudioItemModel>

    suspend fun getAudiosOfGenre(genreId: Long): List<AudioItemModel>

    suspend fun getAlbumByAlbumId(albumId: Long): AlbumItemModel?

    suspend fun getArtistByArtistId(artistId: Long): ArtistItemModel?

    suspend fun getGenreByGenreId(genreId: Long): GenreItemModel?
}