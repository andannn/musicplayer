package com.andannn.melodify.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.andannn.melodify.core.database.entity.LyricEntity
import com.andannn.melodify.core.database.entity.LyricWithAudioCrossRef

internal object Tables {
    const val LYRIC = "lyric_table"
    const val LYRIC_WITH_AUDIO_CROSS_REF = "lyric_with_audio_cross_ref_table"
}

@Database(
    entities = [
        LyricEntity::class,
        LyricWithAudioCrossRef::class,
    ],
    version = 1
)
@ConstructedBy(MelodifyDataBaseConstructor::class)
abstract class MelodifyDataBase : RoomDatabase() {
    abstract fun getLyricDao(): LyricDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object MelodifyDataBaseConstructor : RoomDatabaseConstructor<MelodifyDataBase> {
    override fun initialize(): MelodifyDataBase
}