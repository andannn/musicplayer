package com.andannn.melodify.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andannn.melodify.core.database.entity.LyricColumns
import com.andannn.melodify.core.database.entity.LyricEntity
import com.andannn.melodify.core.database.entity.LyricWithAudioCrossRef
import com.andannn.melodify.core.database.entity.LyricWithAudioCrossRefColumns

@Dao
interface LyricDao {
    @Insert(entity = LyricEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLyricEntities(entities: List<LyricEntity>)

    @Insert(entity = LyricWithAudioCrossRef::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLyricWithAudioCrossRef(crossRefs: List<LyricWithAudioCrossRef>)

    @Query("""
        select * from ${Tables.LYRIC_WITH_AUDIO_CROSS_REF}
        left join ${Tables.LYRIC} 
            on ${LyricColumns.ID} = ${LyricWithAudioCrossRefColumns.LYRIC_ID}
        where :mediaStoreId = ${LyricWithAudioCrossRefColumns.MEDIA_STORE_ID}
    """)
    suspend fun getLyricByMediaStoreId(mediaStoreId: Long): LyricEntity?
}