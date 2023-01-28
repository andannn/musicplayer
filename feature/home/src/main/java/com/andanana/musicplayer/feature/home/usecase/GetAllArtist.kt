package com.andanana.musicplayer.feature.home.usecase

import com.andanana.musicplayer.core.data.repository.LocalMusicRepository
import com.andanana.musicplayer.core.model.ArtistInfo
import javax.inject.Inject

class GetAllArtist @Inject constructor(
    private val repository: LocalMusicRepository
) {
    suspend operator fun invoke(): List<ArtistInfo> {
        return repository.getAllArtistInfo()
    }
}
