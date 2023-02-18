package com.andanana.musicplayer.core.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MusicWithPlayLists(
    @Embedded
    val music: Music,
    @Relation(
        parentColumn = "media_store_id",
        entityColumn = "play_list_id",
        associateBy = Junction(PlayListMusicCrossRef::class)
    )
    val playList: List<PlayList>
)
