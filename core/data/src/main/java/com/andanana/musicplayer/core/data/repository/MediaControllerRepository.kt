package com.andanana.musicplayer.core.data.repository

import com.andanana.musicplayer.core.data.model.AlbumItem
import com.andanana.musicplayer.core.data.model.AudioItem

interface MediaControllerRepository {
    suspend fun getAllMediaItems(): List<AudioItem>

    suspend fun getAudiosOfAlbum(albumId: Long): List<AudioItem>

    suspend fun getAudiosOfArtist(artistId: Long): List<AudioItem>

    suspend fun getAudioCategories(): List<String>

    suspend fun getAlbumByAlbumId(albumId: Long): AlbumItem?

    fun playMediaList(mediaList: List<AudioItem>, index: Int, isShuffle: Boolean)
}