package com.andanana.musicplayer.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.andanana.musicplayer.core.database.entity.Music
import com.andanana.musicplayer.core.database.entity.PlayListMusicCrossRef

@Dao
interface MusicDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnorePlayListMusicCrossRefEntities(
        playListMusicCrossRefReferences: List<PlayListMusicCrossRef>
    )

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreMusicEntities(
        entities: List<Music>
    )
}

