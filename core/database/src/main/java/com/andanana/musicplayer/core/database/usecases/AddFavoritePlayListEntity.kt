package com.andanana.musicplayer.core.database.usecases

import com.andanana.musicplayer.core.database.dao.PlayListDao
import com.andanana.musicplayer.core.database.entity.PlayListEntity
import javax.inject.Inject

const val FAVORITE_PLAY_LIST_ID = 10000L

class AddFavoritePlayListEntity @Inject constructor(
    private val playListDao: PlayListDao
) {
    suspend operator fun invoke(date: Long) {
        playListDao.insertPlayListEntities(
            PlayListEntity(
                playListId = FAVORITE_PLAY_LIST_ID,
                name = "Favorite",
                createdDate = date
            )
        )
    }
}
