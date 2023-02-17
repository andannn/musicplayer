package com.andanana.musicplayer.core.database.usecases

import com.andanana.musicplayer.core.database.dao.PlayListDao
import com.andanana.musicplayer.core.database.entity.PlayListWithoutId
import javax.inject.Inject

class AddPlayListEntity @Inject constructor(
    private val playListDao: PlayListDao
) {
    suspend operator fun invoke(playListName: String, createdDate: Long) {
        playListDao.insertPlayListEntities(
            PlayListWithoutId(
                name = playListName,
                createdDate = createdDate
            )
        )
    }
}
