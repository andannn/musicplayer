package com.andanana.musicplayer.core.database.usecases

import com.andanana.musicplayer.core.database.dao.MusicDao
import com.andanana.musicplayer.core.database.dao.PlayListDao
import com.andanana.musicplayer.core.database.entity.PlayListMusicCrossRef
import javax.inject.Inject

class DeletePlayList @Inject constructor(
    private val playListDao: PlayListDao,
    private val musicDao: MusicDao
) {
    suspend operator fun invoke(playListId: Long) {
        val allMusicInPlayList = musicDao.getPlayListWithMusics(playListId)?.musics
            ?: emptyList()
        playListDao.deleteMusicInPlaylist(
            allMusicInPlayList.map {
                PlayListMusicCrossRef(
                    playListId = playListId,
                    musicId = it.mediaStoreId
                )
            }
        )
        playListDao.deletePlaylist(
            playListId = playListId
        )
    }
}
