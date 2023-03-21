package com.andanana.musicplayer.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.andanana.musicplayer.core.database.entity.MusicWithPlayLists
import com.andanana.musicplayer.core.database.entity.PlayList
import com.andanana.musicplayer.core.database.entity.PlayListMusicCount
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
    suspend fun insertPlayListEntities(
        entities: PlayList
    ): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnorePlayListMusicCrossRefEntities(
        playListMusicCrossRefReferences: List<PlayListMusicCrossRef>
    )

    @Query("SELECT * FROM play_list")
    fun getAllPlaylist(): Flow<List<PlayList>>

    @Query("SELECT * FROM play_list WHERE play_list_id = :playListId")
    fun getPlaylistByPlayListId(playListId: Long): PlayList

    @Delete
    suspend fun deleteMusicInPlaylist(playListMusicCrossRefReferences: List<PlayListMusicCrossRef>)

    @Transaction
    @Query("SELECT * FROM music WHERE media_store_id = :mediaId")
    fun getMusicWithPlayLists(mediaId: Long): Flow<MusicWithPlayLists?>

    @Query("SELECT COUNT(*) FROM play_list_music WHERE play_list_id = :playListId")
    fun getMusicCountByPlayListId(playListId: Long): Int

    @Query("SELECT play_list_id AS playListId, COUNT(*) AS musicCount FROM play_list_music GROUP BY play_list_id")
    fun getAllMusicCounts(): Flow<List<PlayListMusicCount>>
}
