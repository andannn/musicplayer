package com.andanana.musicplayer.core.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PlayListWithMusics(
    @Embedded
    val playListEntity: PlayListEntity,
    @Relation(
        parentColumn = "${PlayListColumns.id}",
        entityColumn = MusicColumns.id,
        associateBy = Junction(PlayListMusicCrossRef::class)
    )
    val musicEntities: List<MusicEntity>
)
