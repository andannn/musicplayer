package com.andanana.musicplayer.core.data.data

import com.andanana.musicplayer.core.data.model.AlbumData
import com.andanana.musicplayer.core.data.model.ArtistData
import com.andanana.musicplayer.core.data.model.AudioData

interface MediaStoreSource {
    suspend fun getAllMusicData(): List<AudioData>

    suspend fun getAllAlbumData(): List<AlbumData>

    suspend fun getAllArtistData(): List<ArtistData>
}
