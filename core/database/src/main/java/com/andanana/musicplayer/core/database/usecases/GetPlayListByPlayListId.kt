package com.andanana.musicplayer.core.database.usecases

import com.andanana.musicplayer.core.database.dao.PlayListDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPlayListByPlayListId @Inject constructor(
    private val playListDao: PlayListDao
) {
    suspend operator fun invoke(playListId: Long) = withContext(Dispatchers.IO) {
        playListDao.getPlaylistByPlayListId(playListId)
    }
}
