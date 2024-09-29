package com.andannn.melodify.core.data.repository

import com.andannn.melodify.core.data.util.toLyricEntity
import com.andannn.melodify.core.data.util.toLyricModel
import com.andannn.melodify.core.database.LyricDao
import com.andannn.melodify.core.domain.repository.LyricRepository
import com.andannn.melodify.core.network.LrclibService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "LyricRepository"

@Singleton
class LyricRepositoryImpl
@Inject
constructor(
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
            Timber.tag(TAG).d("Error getting lyric: ${e.message}")
            return
        }
    }

    override fun getLyricByMediaStoreIdFlow(mediaStoreId: Long) =
        lyricDao.getLyricByMediaStoreIdFlow(mediaStoreId).map {
            it?.toLyricModel()
        }
}