package com.andanana.musicplayer.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andanana.musicplayer.core.data.model.AudioData
import com.andanana.musicplayer.core.database.Tables

object MusicColumns {
    const val id = "id"
    const val title = "title"
    const val duration = "duration"
    const val dateModified = "date_modified"
    const val size = "size"
    const val mimeType = "mime_type"
    const val data = "data"
    const val album = "album"
    const val artist = "artist"
    const val albumId = "album_id"
    const val artistId = "artist_id"
    const val cdTrackNumber = "cd_track_number"
    const val discNumber = "disc_number"
}

@Entity(tableName = Tables.music)
data class MusicEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = MusicColumns.id)
    val id: Long,
    @ColumnInfo(name = MusicColumns.title, defaultValue = "")
    val title: String = "",
    @ColumnInfo(name = MusicColumns.duration)
    val duration: Int = -1,
    @ColumnInfo(name = MusicColumns.dateModified)
    val modifiedDate: Long = -1,
    @ColumnInfo(name = MusicColumns.size)
    val size: Int = -1,
    @ColumnInfo(name = MusicColumns.mimeType)
    val mimeType: String = "",
    @ColumnInfo(name = MusicColumns.album)
    val album: String = "",
    @ColumnInfo(name = MusicColumns.albumId)
    val albumId: Long = -1,
    @ColumnInfo(name = MusicColumns.artist)
    val artist: String = "",
    @ColumnInfo(name = MusicColumns.artistId)
    val artistId: Long = -1,
    @ColumnInfo(name = MusicColumns.cdTrackNumber)
    val cdTrackNumber: Int = -1,
    @ColumnInfo(name = MusicColumns.discNumber)
    val discNumber: Int = -1
)

fun AudioData.asEntity(): MusicEntity = MusicEntity(
    id = this.id,
    title = this.title,
    duration = this.duration,
    modifiedDate = this.modifiedDate,
    size = this.size,
    mimeType = this.mimeType,
    album = this.album,
    albumId = this.albumId,
    artist = this.artist,
    artistId = this.artistId,
    cdTrackNumber = this.cdTrackNumber,
    discNumber = this.discNumberIndex
)