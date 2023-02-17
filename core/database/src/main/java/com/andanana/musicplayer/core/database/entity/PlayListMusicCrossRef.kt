package com.andanana.musicplayer.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "play_list_music",
    primaryKeys = ["play_list_id", "media_store_id"],
    foreignKeys = [
        ForeignKey(
            entity = PlayList::class,
            parentColumns = ["play_list_id"],
            childColumns = ["play_list_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Music::class,
            parentColumns = ["media_store_id"],
            childColumns = ["media_store_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["play_list_id"]),
        Index(value = ["media_store_id"])
    ]
)
data class PlayListMusicCrossRef(
    @ColumnInfo(name = "play_list_id")
    val playListId: Long,
    @ColumnInfo(name = "media_store_id")
    val musicId: Long
)


