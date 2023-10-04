package com.andanana.musicplayer.core.data.model

import android.net.Uri
import android.provider.MediaStore
import com.andanana.musicplayer.core.database.entity.ArtistEntity

data class ArtistModel(
    val artistId: Long,
    val artistUri: Uri = Uri.EMPTY,
    val name: String,
    val trackCount: Int = 0
)

fun ArtistEntity.toArtistModel() =
    ArtistModel(
        artistId = this.artistId,
        artistUri = Uri.withAppendedPath(
            MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
            this.artistId.toString()
        ),
        name = this.name,
        trackCount = this.trackCount
    )

fun List<ArtistEntity>.toArtistModels() = map { entity ->
    entity.toArtistModel()
}