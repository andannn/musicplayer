package com.andannn.melodify.core.data.model


enum class MediaListSource {
    ALBUM,
    GENRE,
    ARTIST;

    fun toNavArg() = this.name

    companion object {
        fun fromString(string: String) = entries.firstOrNull {
            it.name == string
        }
    }
}