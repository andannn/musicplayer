package com.andanana.musicplayer.core.data.data

import com.andanana.musicplayer.core.data.model.AlbumData
import com.andanana.musicplayer.core.data.model.ArtistData
import com.andanana.musicplayer.core.data.model.AudioData

interface MediaStoreSource {
    suspend fun getAllMusicInfo(): List<AudioData>

    suspend fun getAllAlbumInfo(): List<AlbumData>

    suspend fun getAllArtistInfo(): List<ArtistData>
}
