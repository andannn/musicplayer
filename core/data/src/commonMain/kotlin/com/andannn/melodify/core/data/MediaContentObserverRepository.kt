package com.andannn.melodify.core.data

import kotlinx.coroutines.flow.Flow

interface MediaContentObserverRepository {
    val allAlbumUri: String

    val allArtistUri: String

    val allAudioUri: String

    fun getAlbumUri(albumId: Long): String

    fun getArtistUri(artistId: Long): String

    fun getContentChangedEventFlow(uri: String): Flow<Unit>
}