package com.andanana.musicplayer.core.data.model

import android.net.Uri
import android.provider.MediaStore
import com.andanana.musicplayer.core.database.entity.MusicEntity

data class MusicModel(
    val id: Long,
    val contentUri: Uri = Uri.EMPTY,
    val title: String = "",
    val duration: Int = 0,
    val modifiedDate: Long = 0,
    val size: Int = 0,
    val mimeType: String = "",
    val absolutePath: String = "",
    val album: String = "",
    val artist: String = "",
    val albumUri: Uri = Uri.EMPTY,
    val cdTrackNumber: Int = 0,
    val discNumberIndex: Int = 0
)

fun MusicEntity.toMusicModel() = MusicModel(
    id = this.id,
    contentUri = Uri.withAppendedPath(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        this.id.toString()
    ),
    title = this.title,
    duration = this.duration,
    modifiedDate = this.modifiedDate,
    size = this.size,
    mimeType = this.mimeType,
//TODO:
    absolutePath = "",
    album = this.album,
    artist = this.artist,
    albumUri = Uri.withAppendedPath(
        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
        this.albumId.toString()
    ),
    cdTrackNumber = this.cdTrackNumber,
    discNumberIndex = this.discNumber
)

fun List<MusicEntity>.toMusicModels(): List<MusicModel> = map { entity ->
    entity.toMusicModel()
}