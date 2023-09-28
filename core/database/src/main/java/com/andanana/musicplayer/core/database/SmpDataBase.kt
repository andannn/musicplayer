package com.andanana.musicplayer.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andanana.musicplayer.core.database.dao.MusicDao
import com.andanana.musicplayer.core.database.dao.PlayListDao
import com.andanana.musicplayer.core.database.entity.MusicEntity
import com.andanana.musicplayer.core.database.entity.PlayList
import com.andanana.musicplayer.core.database.entity.PlayListMusicCrossRef

@Database(
    entities = [
        PlayList::class,
        MusicEntity::class,
        PlayListMusicCrossRef::class
    ],
    version = 1
)
abstract class SmpDataBase : RoomDatabase() {
    abstract fun playListDao(): PlayListDao
    abstract fun musicDao(): MusicDao
}
