package com.andanana.musicplayer.core.database.usecases

import android.util.Log
import com.andanana.musicplayer.core.database.dao.PlayListDao
import com.andanana.musicplayer.core.database.entity.PlayListMusicCrossRef
import javax.inject.Inject

private const val TAG = "AddMusicToFavorite"

class AddMusicToFavorite @Inject constructor(
    private val playListDao: PlayListDao
) {
    suspend operator fun invoke(musicMediaId: Long) {
        Log.d(TAG, "invoke: $musicMediaId")
        playListDao.insertOrIgnorePlayListMusicCrossRefEntities(
            listOf(
                PlayListMusicCrossRef(
                    playListId = FAVORITE_PLAY_LIST_ID,
                    musicId = musicMediaId
                )
            )
        )
    }
}
