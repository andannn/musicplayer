package com.andanana.musicplayer.core.data.repository

import com.andanana.musicplayer.core.model.MusicItem

interface LocalMusicRepository {
    suspend fun getLocalMusicItems(): List<MusicItem>
}
