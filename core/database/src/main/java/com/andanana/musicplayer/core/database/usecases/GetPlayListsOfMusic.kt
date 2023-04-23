package com.andanana.musicplayer.core.database.usecases

import com.andanana.musicplayer.core.database.dao.PlayListDao
import com.andanana.musicplayer.core.database.entity.PlayList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPlayListsOfMusic @Inject constructor(
    private val playListDao: PlayListDao
) {
    operator fun invoke(mediaId: Long): Flow<List<PlayList>> {
        return playListDao.getMusicWithPlayLists(mediaId).map {
            it?.playList?.sortedByDescending { it.createdDate } ?: emptyList()
        }
    }
}
