package com.andannn.melodify.core.data.model

sealed interface MediaItemModel {
    val id: Long
    val name: String
    val artWorkUri: String
}

data class AudioItemModel(
    override val id: Long,
    override val name: String,
    override val artWorkUri: String,
    val modifiedDate: Long,
    val album: String,
    val albumId: Long,
    val artist: String,
    val artistId: Long,
    val cdTrackNumber: Int,
    val discNumberIndex: Int,
    val extraUniqueId: String? = null,
) : MediaItemModel {
    companion object {
        val DEFAULT = AudioItemModel(0, "", "", 0, "", 0, "", 0, 0, 0)
    }
}

data class AlbumItemModel(
    override val id: Long,
    override val name: String,
    override val artWorkUri: String,
    val trackCount: Int,
) : MediaItemModel {
    companion object {
        val DEFAULT = AlbumItemModel(0, "", "", 0)
    }
}

data class ArtistItemModel(
    override val id: Long,
    override val name: String,
    override val artWorkUri: String,
    val trackCount: Int,
) : MediaItemModel {
    companion object {
        val DEFAULT = ArtistItemModel(0, "", "", 0)
    }
}

data class GenreItemModel constructor(
    override val id: Long,
    override val name: String,
    override val artWorkUri: String,
    val trackCount: Int,
) : MediaItemModel {
    companion object {
        val DEFAULT = GenreItemModel(0, "", "", 0)
        val UNKNOWN = GenreItemModel(-1, "", "", 0)
    }
}