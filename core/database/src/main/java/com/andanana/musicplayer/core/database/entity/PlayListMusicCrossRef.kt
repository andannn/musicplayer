package com.andanana.musicplayer.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "play_list_music",
    primaryKeys = ["play_list_id", "music_id"],
    foreignKeys = [
        ForeignKey(
            entity = PlayList::class,
            parentColumns = ["id"],
            childColumns = ["play_list_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Music::class,
            parentColumns = ["id"],
            childColumns = ["music_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["play_list_id"]),
        Index(value = ["music_id"])
    ]
)
data class PlayListMusicCrossRef(
    @ColumnInfo(name = "play_list_id")
    val playListId: Int,
    @ColumnInfo(name = "music_id")
    val musicId: Int
)


