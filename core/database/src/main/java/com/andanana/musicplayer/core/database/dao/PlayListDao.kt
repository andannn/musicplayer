package com.andanana.musicplayer.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.andanana.musicplayer.core.database.Tables
import com.andanana.musicplayer.core.database.entity.MusicColumns
import com.andanana.musicplayer.core.database.entity.MusicWithPlayLists
import com.andanana.musicplayer.core.database.entity.PlayListEntity
import com.andanana.musicplayer.core.database.entity.PlayListColumns
import com.andanana.musicplayer.core.database.entity.PlayListMusicCount
import com.andanana.musicplayer.core.database.entity.PlayListMusicCrossRef
import com.andanana.musicplayer.core.database.entity.PlayListMusicCrossRefColumns
import com.andanana.musicplayer.core.database.entity.PlayListWithoutId
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayListDao {

    @Insert(entity = PlayListEntity::class)
    suspend fun insertPlayListEntities(
        entities: PlayListWithoutId
    ): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlayListEntities(
        entities: PlayListEntity
    ): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnorePlayListMusicCrossRefEntities(
        playListMusicCrossRefReferences: List<PlayListMusicCrossRef>
    )

    @Query("SELECT * FROM ${Tables.musicPlayListCrossRef} WHERE ${PlayListMusicCrossRefColumns.playListCrossRef} = :playListId AND ${PlayListMusicCrossRefColumns.musicCrossRef} = :musicId")
    fun getPlayListMusicCrossRef(playListId: Long, musicId: Long): PlayListMusicCrossRef

    @Query("SELECT * FROM ${Tables.playList}")
    fun getAllPlaylist(): Flow<List<PlayListEntity>>

    @Query("SELECT * FROM ${Tables.playList} WHERE ${PlayListColumns.id} = :playListId")
    fun getPlaylistByPlayListId(playListId: Long): PlayListEntity

    @Delete
    suspend fun deleteMusicInPlaylist(playListMusicCrossRefReferences: List<PlayListMusicCrossRef>)

    @Query("DELETE FROM ${Tables.playList} WHERE ${PlayListColumns.id} = :playListId")
    suspend fun deletePlaylist(playListId: Long)

    @Transaction
    @Query("SELECT * FROM ${Tables.music} WHERE ${MusicColumns.id} = :mediaId")
    fun getMusicWithPlayLists(mediaId: Long): Flow<MusicWithPlayLists?>

    @Query("SELECT COUNT(*) FROM ${Tables.musicPlayListCrossRef} WHERE ${PlayListColumns.id} = :playListId")
    fun getMusicCountByPlayListId(playListId: Long): Int

    @Query("SELECT ${PlayListColumns.id} AS playListId, COUNT(*) AS musicCount FROM ${Tables.musicPlayListCrossRef} GROUP BY ${PlayListColumns.id}")
    fun getAllMusicCounts(): Flow<List<PlayListMusicCount>>
}
