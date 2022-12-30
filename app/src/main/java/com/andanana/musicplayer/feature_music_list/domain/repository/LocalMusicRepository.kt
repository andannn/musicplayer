package com.andanana.musicplayer.feature_music_list.domain.repository

import com.andanana.musicplayer.feature_music_list.domain.model.MusicItem

interface LocalMusicRepository {
    suspend fun getLocalMusicItems(): List<MusicItem>
}