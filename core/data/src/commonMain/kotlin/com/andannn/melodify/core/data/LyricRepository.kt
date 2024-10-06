package com.andannn.melodify.core.data

import com.andannn.melodify.core.data.model.LyricModel
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