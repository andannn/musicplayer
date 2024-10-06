package com.andannn.melodify.core.data.repository

import com.andannn.melodify.core.data.util.toLyricEntity
import com.andannn.melodify.core.data.util.toLyricModel
import com.andannn.melodify.core.database.LyricDao
import com.andannn.melodify.core.data.LyricRepository
import com.andannn.melodify.core.network.LrclibService
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val TAG = "LyricRepository"

class LyricRepositoryImpl(
    private val lyricDao: LyricDao,
    private val lyricLocalDataSource: LrclibService
) : LyricRepository {
    override suspend fun tryGetLyricOrIgnore(
        mediaStoreId: Long,
        trackName: String,
        artistName: String,
        albumName: String?,
        duration: Long?,
    ) {
        val lyric = lyricDao.getLyricByMediaStoreIdFlow(mediaStoreId).first()
        if (lyric != null) return

        try {
            val lyricData = lyricLocalDataSource.getLyric(
                trackName = trackName,
                artistName = artistName,
                albumName = albumName,
                duration = duration
            )
            lyricDao.insertLyricOfMedia(
                mediaStoreId = mediaStoreId,
                lyric = lyricData.toLyricEntity()
            )
        } catch (e: Exception) {
            Napier.d(tag = TAG) { "Error getting lyric: ${e.message}" }
            return
        }
    }

    override fun getLyricByMediaStoreIdFlow(mediaStoreId: Long) =
        lyricDao.getLyricByMediaStoreIdFlow(mediaStoreId).map {
            it?.toLyricModel()
        }
}