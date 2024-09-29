package com.andannn.melodify.core.network.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LyricData(
    @SerialName(value = "id")
    val id: Long,

    @SerialName(value = "name")
    val name: String,

    @SerialName(value = "trackName")
    val trackName: String,

    @SerialName(value = "artistName")
    val artistName: String,

    @SerialName(value = "albumName")
    val albumName: String,

    @SerialName(value = "duration")
    val duration: Double,

    @SerialName(value = "instrumental")
    val instrumental: Boolean,

    @SerialName(value = "plainLyrics")
    val plainLyrics: String,

    @SerialName(value = "syncedLyrics")
    val syncedLyrics: String,
)
