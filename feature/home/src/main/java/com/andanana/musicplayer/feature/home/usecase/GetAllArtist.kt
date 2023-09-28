package com.andanana.musicplayer.feature.home.usecase

import com.andanana.musicplayer.core.data.data.MediaStoreSource
import com.andanana.musicplayer.core.model.ArtistInfo
import javax.inject.Inject

class GetAllArtist @Inject constructor(
    private val repository: MediaStoreSource
) {
    suspend operator fun invoke(): List<ArtistInfo> {
//        return repository.getAllArtistInfo()
        return emptyList()
    }
}
