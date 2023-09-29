package com.andanana.musicplayer.core.database.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andanana.musicplayer.core.database.Tables

object ArtistColumns {
    const val id = "artist_id"
    const val artistName = "artist_name"
    const val trackCount = "track_count"
}

@Entity(tableName = Tables.artist)
data class ArtistEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ArtistColumns.id)
    val artistId: Int,
    @ColumnInfo(name = ArtistColumns.artistName)
    val name: String = "",
    @ColumnInfo(name = ArtistColumns.trackCount)
    val trackCount: Int = 0
)
