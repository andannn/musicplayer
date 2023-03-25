package com.andanana.musicplayer.core.database.usecases

import com.andanana.musicplayer.core.database.dao.PlayListDao
import com.andanana.musicplayer.core.database.entity.PlayListMusicCrossRef
import javax.inject.Inject

class AddMusicToPlayList @Inject constructor(
    private val playListDao: PlayListDao
) {
    suspend operator fun invoke(musicMediaId: Long, playlistId: Long, addedDate: Long) {
        playListDao.insertOrIgnorePlayListMusicCrossRefEntities(
            listOf(
                PlayListMusicCrossRef(
                    playListId = playlistId,
                    musicId = musicMediaId,
                    musicAddedDate = addedDate
                )
            )
        )
    }
}
