package com.andanana.musicplayer.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andanana.musicplayer.core.database.Tables

object AlbumColumns {
    const val id = "album_id"
    const val title = "album_title"
    const val trackCount = "track_count"
}

@Entity(tableName = Tables.artist)
data class AlbumEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = AlbumColumns.id)
    val artistId: Int,
    @ColumnInfo(name = AlbumColumns.title)
    val title: String = "",
    @ColumnInfo(name = AlbumColumns.trackCount)
    val trackCount: Int = 0
)
