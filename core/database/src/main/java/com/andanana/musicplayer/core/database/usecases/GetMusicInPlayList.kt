package com.andanana.musicplayer.core.database.usecases

import com.andanana.musicplayer.core.database.dao.MusicDao
import com.andanana.musicplayer.core.database.dao.PlayListDao
import com.andanana.musicplayer.core.database.entity.MusicWithAddedTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMusicInPlayList @Inject constructor(
    private val playListDao: PlayListDao,
    private val musicDao: MusicDao
) {
    operator fun invoke(playListId: Long): Flow<List<MusicWithAddedTime>> {
        return musicDao.getPlayListWithMusicsFlow(playListId).map {
            val playList = it?.playListEntity
            val musics = it?.musicEntities ?: emptyList()
            playList?.let { playList ->
                musics.map { music ->
                    val crossRef = getPlayListMusicCrossRef(
                        playList.playListId,
                        musicId = music.id
                    )
                    MusicWithAddedTime(
                        musicEntity = music,
                        musicAddedDate = crossRef.musicAddedDate
                    )
                }
            } ?: emptyList()
        }
    }

    private suspend fun getPlayListMusicCrossRef(
        playListId: Long,
        musicId: Long
    ) = withContext(Dispatchers.IO) {
        playListDao.getPlayListMusicCrossRef(
            playListId,
            musicId = musicId
        )
    }
}
