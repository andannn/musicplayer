package com.andannn.melodify.core.data.repository

import com.andannn.melodify.core.data.MediaContentObserverRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private const val TAG = "MediaContentObserverRepository"

internal class MediaContentObserverRepositoryImpl(
) : MediaContentObserverRepository {
    override val allAlbumUri: String
        get() = ""
    override val allArtistUri: String
        get() = ""
    override val allAudioUri: String
        get() = ""

    override fun getAlbumUri(albumId: Long): String {
        return ""
    }

    override fun getArtistUri(artistId: Long): String {
        return ""
    }

    override fun getContentChangedEventFlow(uri: String): Flow<Unit> = flow {
        emit(Unit)
    }
}