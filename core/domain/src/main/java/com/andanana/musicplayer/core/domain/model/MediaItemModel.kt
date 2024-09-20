package com.andanana.musicplayer.core.domain.model

sealed interface MediaItemModel {
    val id: Long
    val name: String
}

data class AudioItemModel(
    override val id: Long,
    override val name: String,
    val modifiedDate: Long,
    val album: String,
    val albumId: Long,
    val artist: String,
    val artistId: Long,
    val cdTrackNumber: Int,
    val discNumberIndex: Int,
    val artWorkUri: String,
) : MediaItemModel {
    companion object {
        val DEFAULT = AudioItemModel(0, "", 0, "", 0, "", 0, 0, 0, "")
    }
}

data class AlbumItemModel(
    override val id: Long,
    override val name: String,
    val trackCount: Int,
    val artWorkUri: String,
) : MediaItemModel {
    companion object {
        val DEFAULT = AlbumItemModel(0, "", 0, "")
    }
}

data class ArtistItemModel(
    override val id: Long,
    override val name: String,
    val trackCount: Int,
    val artistCoverUri: String,
) : MediaItemModel {
    companion object {
        val DEFAULT = ArtistItemModel(0, "", 0, "")
    }
}
