package com.andannn.melodify.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface MediaContentObserverRepository {
    fun getAlbumUri(albumId: Long): String

    fun getArtistUri(artistId: Long): String

    fun getContentChangedEventFlow(uri: String): Flow<Unit>
}