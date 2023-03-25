package com.andanana.musicplayer.core.database.usecases

import com.andanana.musicplayer.core.database.dao.PlayListDao
import com.andanana.musicplayer.core.database.entity.PlayListMusicCrossRef
import javax.inject.Inject

class DeleteMusicInFavorite @Inject constructor(
    private val playListDao: PlayListDao
) {
    suspend operator fun invoke(musicMediaId: Long) {
//        playListDao.deleteMusicInPlaylist(
//            listOf(
//                PlayListMusicCrossRef(
//                    playListId = FAVORITE_PLAY_LIST_ID,
//                    musicId = musicMediaId
//                )
//            )
//        )
    }
}
