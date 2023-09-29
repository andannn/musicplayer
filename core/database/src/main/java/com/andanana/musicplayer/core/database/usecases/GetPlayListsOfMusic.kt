package com.andanana.musicplayer.core.database.usecases

import com.andanana.musicplayer.core.database.dao.PlayListDao
import com.andanana.musicplayer.core.database.entity.PlayListEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPlayListsOfMusic @Inject constructor(
    private val playListDao: PlayListDao
) {
    operator fun invoke(mediaId: Long): Flow<List<PlayListEntity>> {
        return playListDao.getMusicWithPlayLists(mediaId).map {
            it?.playListEntity?.sortedByDescending { it.createdDate } ?: emptyList()
        }
    }
}
