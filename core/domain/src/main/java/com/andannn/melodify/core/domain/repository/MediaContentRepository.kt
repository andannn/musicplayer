package com.andannn.melodify.core.domain.repository

import com.andannn.melodify.core.domain.model.AlbumItemModel
import com.andannn.melodify.core.domain.model.ArtistItemModel
import com.andannn.melodify.core.domain.model.AudioItemModel

interface MediaContentRepository {
    suspend fun getAllMediaItems(): List<AudioItemModel>

    suspend fun getAllAlbums(): List<AlbumItemModel>

    suspend fun getAllArtist(): List<ArtistItemModel>

    suspend fun getAudiosOfAlbum(albumId: Long): List<AudioItemModel>

    suspend fun getAudiosOfArtist(artistId: Long): List<AudioItemModel>

    suspend fun getAlbumByAlbumId(albumId: Long): AlbumItemModel?

    suspend fun getArtistByAlbumId(artistId: Long): ArtistItemModel?
}