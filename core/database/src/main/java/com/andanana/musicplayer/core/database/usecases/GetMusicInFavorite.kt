package com.andanana.musicplayer.core.database.usecases

import com.andanana.musicplayer.core.database.dao.MusicDao
import com.andanana.musicplayer.core.database.dao.PlayListDao
import com.andanana.musicplayer.core.database.entity.MusicWithAddedTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMusicInFavorite @Inject constructor(
    private val playListDao: PlayListDao,
    private val musicDao: MusicDao
) {
    operator fun invoke(): Flow<List<MusicWithAddedTime>> {
        return musicDao.getPlayListWithMusicsFlow(FAVORITE_PLAY_LIST_ID).map {
            val playList = it?.playListEntity
            val musics = it?.musicEntities ?: emptyList()
            playList?.let { playList ->
                musics.map { music ->
                    val crossRef = getPlayListMusicCrossRef(
                        playList.playListId,
                        musicId = music.id.toLong()
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
