package com.andanana.musicplayer.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andanana.musicplayer.core.database.entity.PlayList
import com.andanana.musicplayer.core.database.entity.PlayListMusicCrossRef
import com.andanana.musicplayer.core.database.entity.PlayListWithoutId
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayListDao {

    @Insert(entity = PlayList::class)
    suspend fun insertPlayListEntities(
        entities: PlayListWithoutId
    ): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnorePlayListMusicCrossRefEntities(
        playListMusicCrossRefReferences: List<PlayListMusicCrossRef>
    )

    @Query("SELECT * FROM play_list")
    fun getAllPlaylist(): Flow<List<PlayList>>
}
