package com.andannn.melodify.core.network

import com.andannn.melodify.core.network.model.LyricData
import com.andannn.melodify.core.network.resources.ApiRes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get

interface LrclibService {
    /**
     * Get lyric from lrclib
     *
     * @param trackName Title of the track
     * @param artistName Name of the artist
     * @param albumName Name of the album (optional)
     * @param duration Track's duration in seconds (optional)
     */
    suspend fun getLyric(
        trackName: String,
        artistName: String,
        albumName: String? = null,
        duration: Long? = null,
    ): LyricData
}


internal class LrclibServiceImpl(
    private val httpClient: HttpClient,
) : LrclibService {

    override suspend fun getLyric(
        trackName: String,
        artistName: String,
        albumName: String?,
        duration: Long?
    ): LyricData = httpClient
        .get(
            ApiRes.Get(
                track_name = trackName,
                artist_name = artistName,
                album_name = albumName,
                duration = duration
            )
        )
        .body()
}