package com.andanana.musicplayer.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andanana.musicplayer.core.database.dao.MusicDao
import com.andanana.musicplayer.core.database.dao.PlayListDao
import com.andanana.musicplayer.core.database.entity.MusicEntity
import com.andanana.musicplayer.core.database.entity.PlayListEntity
import com.andanana.musicplayer.core.database.entity.PlayListMusicCrossRef

object Tables {
    const val music = "music_table"
    const val artist = "artist_table"
    const val playList = "play_list_table"
    const val musicPlayListCrossRef = "music_play_list_cross_ref_table"
}

@Database(
    entities = [
        PlayListEntity::class,
        MusicEntity::class,
        PlayListMusicCrossRef::class
    ],
    version = 1
)
abstract class SmpDataBase : RoomDatabase() {
    abstract fun playListDao(): PlayListDao
    abstract fun musicDao(): MusicDao
}
