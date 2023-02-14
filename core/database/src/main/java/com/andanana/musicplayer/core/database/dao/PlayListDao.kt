package com.andanana.musicplayer.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.andanana.musicplayer.core.database.entity.PlayList

@Dao
interface PlayListDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnorePlayListEntities(
        entities: List<PlayList>
    )
}
