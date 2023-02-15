package com.andanana.musicplayer.core.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PlayListWithMusics(
    @Embedded
    val playList: PlayList,
    @Relation(
        parentColumn = "play_list_id",
        entityColumn = "media_store_id",
        associateBy = Junction(PlayListMusicCrossRef::class)
    )
    val musics: List<Music>
)
