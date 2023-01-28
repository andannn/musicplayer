package com.andanana.musicplayer.feature.home.usecase

import com.andanana.musicplayer.core.data.repository.LocalMusicRepository
import com.andanana.musicplayer.core.model.AlbumInfo
import javax.inject.Inject

class GetAllAlbum @Inject constructor(
    private val repository: LocalMusicRepository
) {
    suspend operator fun invoke(): List<AlbumInfo> {
        return repository.getAllAlbumInfo()
    }
}
