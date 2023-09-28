package com.andanana.musicplayer.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.andanana.musicplayer.core.database.entity.MusicEntity
import com.andanana.musicplayer.core.database.entity.PlayListWithMusics
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreMusicEntities(
        entities: List<MusicEntity>
    )

    @Transaction
    @Query("SELECT * FROM play_list WHERE play_list_id = :playListId")
    fun getPlayListWithMusicsFlow(playListId: Long): Flow<PlayListWithMusics?>

    @Transaction
    @Query("SELECT * FROM play_list WHERE play_list_id = :playListId")
    suspend fun getPlayListWithMusics(playListId: Long): PlayListWithMusics?
}
