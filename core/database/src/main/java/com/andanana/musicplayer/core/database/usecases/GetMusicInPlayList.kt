package com.andanana.musicplayer.core.database.usecases

import com.andanana.musicplayer.core.database.dao.MusicDao
import com.andanana.musicplayer.core.database.entity.Music
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMusicInPlayList @Inject constructor(
    private val musicDao: MusicDao
) {
    operator fun invoke(playListId: Long): Flow<List<Long>> {
        return musicDao.getPlayListWithMusics(playListId).map {
            it?.musics?.map { music: Music -> music.mediaStoreId } ?: emptyList()
        }
    }
}
