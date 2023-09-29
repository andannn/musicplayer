package com.andanana.musicplayer.core.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MusicWithPlayLists(
    @Embedded
    val musicEntity: MusicEntity,
    @Relation(
        parentColumn = MusicColumns.id,
        entityColumn = PlayListColumns.id,
        associateBy = Junction(PlayListMusicCrossRef::class)
    )
    val playListEntity: List<PlayListEntity>
)
