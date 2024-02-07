package com.andanana.musicplayer.core.data.repository

import com.andanana.musicplayer.core.model.PlayMode
import com.andanana.musicplayer.core.model.UserSetting
import kotlinx.coroutines.flow.Flow

interface SmpPreferenceRepository {
    val userData: Flow<UserSetting>

    val playMode: Flow<PlayMode>

    val isShuffle: Flow<Boolean>

    suspend fun setPlayMode(playMode: PlayMode)

    suspend fun setIsShuffle(isShuffle: Boolean)
}
