package com.andanana.musicplayer.core.datastore.repository

import com.andanana.musicplayer.core.model.PlayMode
import com.andanana.musicplayer.core.model.UserSetting
import kotlinx.coroutines.flow.Flow

interface SmpPreferenceRepository {
    val userData: Flow<UserSetting>

    suspend fun setPlayMode(playMode: PlayMode)
}