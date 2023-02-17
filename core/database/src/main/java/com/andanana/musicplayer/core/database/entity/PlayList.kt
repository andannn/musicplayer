package com.andanana.musicplayer.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "play_list")
data class PlayList(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "play_list_id")
    val playListId: Long,
    val name: String,
    val createdDate: Long
)

data class PlayListWithoutId(
    val name: String,
    val createdDate: Long
)
