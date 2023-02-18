package com.andanana.musicplayer.core.database.usecases

import com.andanana.musicplayer.core.database.dao.PlayListDao
import javax.inject.Inject

class GetAllPlayList @Inject constructor(
    private val playListDao: PlayListDao
) {
    operator fun invoke() = playListDao.getAllPlaylist()
}
