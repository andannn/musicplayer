package com.andannn.melodify.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.andannn.melodify.core.database.entity.LyricColumns
import com.andannn.melodify.core.database.entity.LyricEntity
import com.andannn.melodify.core.database.entity.LyricWithAudioCrossRef
import com.andannn.melodify.core.database.entity.LyricWithAudioCrossRefColumns

@Dao
interface LyricDao {
    @Transaction
    suspend fun insertLyricOfMedia(mediaStoreId: Long, lyric: LyricEntity) {
        insertLyricEntities(listOf(lyric))
        insertLyricWithMediaCrossRef(
            listOf(
                LyricWithAudioCrossRef(
                    mediaStoreId = mediaStoreId,
                    lyricId = lyric.id
                )
            )
        )
    }

    @Insert(entity = LyricEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLyricEntities(entities: List<LyricEntity>)

    @Insert(entity = LyricWithAudioCrossRef::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLyricWithMediaCrossRef(crossRefs: List<LyricWithAudioCrossRef>)

    @Query(
        """
        select * from ${Tables.LYRIC_WITH_AUDIO_CROSS_REF}
        left join ${Tables.LYRIC} 
            on ${LyricColumns.ID} = ${LyricWithAudioCrossRefColumns.LYRIC_ID}
        where :mediaStoreId = ${LyricWithAudioCrossRefColumns.MEDIA_STORE_ID}
    """
    )
    suspend fun getLyricByMediaStoreId(mediaStoreId: Long): LyricEntity?
}