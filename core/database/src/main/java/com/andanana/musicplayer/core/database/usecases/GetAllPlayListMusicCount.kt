package com.andanana.musicplayer.core.database.usecases

import com.andanana.musicplayer.core.database.dao.PlayListDao
import com.andanana.musicplayer.core.database.entity.PlayListMusicCount
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPlayListMusicCount @Inject constructor(
    private val playListDao: PlayListDao
) {
    operator fun invoke(): Flow<List<PlayListMusicCount>> {
        return playListDao.getAllMusicCounts()
    }
}
