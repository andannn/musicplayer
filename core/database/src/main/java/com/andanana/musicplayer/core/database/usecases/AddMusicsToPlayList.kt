package com.andanana.musicplayer.core.database.usecases

import com.andanana.musicplayer.core.database.dao.PlayListDao
import com.andanana.musicplayer.core.database.entity.PlayListMusicCrossRef
import javax.inject.Inject

class AddMusicsToPlayList @Inject constructor(
    private val playListDao: PlayListDao
) {
    suspend operator fun invoke(musicMediaIds: List<Long>, playlistId: Long) {
        playListDao.insertOrIgnorePlayListMusicCrossRefEntities(
            musicMediaIds.map { id ->
                PlayListMusicCrossRef(
                    playListId = playlistId,
                    musicId = id
                )
            }
        )
    }
}
