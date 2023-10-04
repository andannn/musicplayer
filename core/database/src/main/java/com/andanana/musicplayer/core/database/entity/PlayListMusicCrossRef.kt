package com.andanana.musicplayer.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.andanana.musicplayer.core.database.Tables


object PlayListMusicCrossRefColumns {
    const val playListCrossRef = "play_list_id"
    const val musicCrossRef = "id"
    const val musicAddedDate = "music_added_date"
}

@Entity(
    tableName = Tables.musicPlayListCrossRef,
    primaryKeys = [PlayListMusicCrossRefColumns.playListCrossRef, PlayListMusicCrossRefColumns.musicCrossRef],
    foreignKeys = [
        ForeignKey(
            entity = PlayListEntity::class,
            parentColumns = [PlayListColumns.id],
            childColumns = [PlayListMusicCrossRefColumns.playListCrossRef],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MusicEntity::class,
            parentColumns = [MusicColumns.id],
            childColumns = [PlayListMusicCrossRefColumns.musicCrossRef],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = [PlayListMusicCrossRefColumns.playListCrossRef]),
        Index(value = [PlayListMusicCrossRefColumns.musicCrossRef])
    ]
)

data class PlayListMusicCrossRef(
    @ColumnInfo(name = PlayListMusicCrossRefColumns.playListCrossRef)
    val playListId: Long,
    @ColumnInfo(name = PlayListMusicCrossRefColumns.musicCrossRef)
    val musicId: Long,
    @ColumnInfo(name = PlayListMusicCrossRefColumns.musicAddedDate)
    val musicAddedDate: Long = 0L
)
