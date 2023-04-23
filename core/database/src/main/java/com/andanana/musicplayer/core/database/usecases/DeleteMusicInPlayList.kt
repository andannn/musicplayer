package com.andanana.musicplayer.core.database.usecases

import com.andanana.musicplayer.core.database.dao.PlayListDao
import com.andanana.musicplayer.core.database.entity.PlayListMusicCrossRef
import javax.inject.Inject

class DeleteMusicInPlayList @Inject constructor(
    private val playListDao: PlayListDao
) {
    suspend operator fun invoke(musicMediaId: Long, playListId: Long) {
        playListDao.deleteMusicInPlaylist(
            listOf(
                PlayListMusicCrossRef(
                    playListId = playListId,
                    musicId = musicMediaId
                )
            )
        )
    }
}
