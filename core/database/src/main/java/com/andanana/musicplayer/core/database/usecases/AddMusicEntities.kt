package com.andanana.musicplayer.core.database.usecases

import com.andanana.musicplayer.core.database.dao.MusicDao
import javax.inject.Inject

class AddMusicEntities @Inject constructor(
    private val musicDao: MusicDao
) {
    suspend operator fun invoke(musicMediaId: List<Long>) {
//        musicDao.insertOrIgnoreMusicEntities(
//            musicMediaId.map { MusicEntity(it) }
//        )
    }
}
