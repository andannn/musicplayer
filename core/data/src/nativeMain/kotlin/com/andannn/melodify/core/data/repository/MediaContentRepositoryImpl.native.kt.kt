package com.andannn.melodify.core.data.repository

import com.andannn.melodify.core.data.MediaContentRepository
import com.andannn.melodify.core.data.model.AlbumItemModel
import com.andannn.melodify.core.data.model.ArtistItemModel
import com.andannn.melodify.core.data.model.AudioItemModel
import com.andannn.melodify.core.data.model.GenreItemModel

internal class MediaContentRepositoryImpl(
) : MediaContentRepository {
    override suspend fun getAllMediaItems(): List<AudioItemModel> {
        return emptyList()
    }

    override suspend fun getAllAlbums(): List<AlbumItemModel> {
        return emptyList()
    }

    override suspend fun getAllArtist(): List<ArtistItemModel> {
        return emptyList()
    }

    override suspend fun getAllGenre(): List<GenreItemModel> {
        return emptyList()
    }

    override suspend fun getAudiosOfAlbum(albumId: Long): List<AudioItemModel> {
        return emptyList()
    }

    override suspend fun getAudiosOfArtist(artistId: Long): List<AudioItemModel> {
        return emptyList()
    }

    override suspend fun getAudiosOfGenre(genreId: Long): List<AudioItemModel> {
        return emptyList()
    }

    override suspend fun getAlbumByAlbumId(albumId: Long): AlbumItemModel? {
        return null
    }

    override suspend fun getArtistByArtistId(artistId: Long): ArtistItemModel? {
        return null
    }

    override suspend fun getGenreByGenreId(genreId: Long): GenreItemModel? {
        return null
    }
}