package com.andanana.musicplayer.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andanana.musicplayer.core.database.Tables

object PlayListColumns {
    const val id = "play_list_id"
    const val name = "name"
    const val createdDate = "created_date"
}

@Entity(tableName = Tables.playList)
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PlayListColumns.id)
    val playListId: Long,
    @ColumnInfo(name = PlayListColumns.name)
    val name: String,
    @ColumnInfo(name = PlayListColumns.createdDate)
    val createdDate: Long
)

data class PlayListWithoutId(
    @ColumnInfo(name = PlayListColumns.name)
    val name: String,
    @ColumnInfo(name = PlayListColumns.createdDate)
    val createdDate: Long
)
