package com.andanana.musicplayer.core.data.model

import android.net.Uri
import android.provider.MediaStore
import com.andanana.musicplayer.core.database.entity.AlbumEntity

data class AlbumModel(
    val albumId: Long,
    val albumUri: Uri = Uri.EMPTY,
    val title: String,
    val trackCount: Int = 0
)

fun AlbumEntity.toAlbumModel() = AlbumModel(
    albumId = this.albumId,
    albumUri = Uri.withAppendedPath(
        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
        this.albumId.toString()
    ),
    title = this.title,
    trackCount = this.trackCount
)


fun List<AlbumEntity>.toAlbumModels() = map { entity ->
    entity.toAlbumModel()
}