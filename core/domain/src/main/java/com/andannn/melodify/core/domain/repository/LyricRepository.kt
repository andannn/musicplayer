package com.andannn.melodify.core.domain.repository

import com.andannn.melodify.core.domain.LyricModel
import kotlinx.coroutines.flow.Flow

interface LyricRepository {
    suspend fun tryGetLyricOrIgnore(
        mediaStoreId: Long,
        trackName: String,
        artistName: String,
        albumName: String? = null,
        duration: Long? = null,
    )

    fun getLyricByMediaStoreIdFlow(mediaStoreId: Long): Flow<LyricModel?>
}