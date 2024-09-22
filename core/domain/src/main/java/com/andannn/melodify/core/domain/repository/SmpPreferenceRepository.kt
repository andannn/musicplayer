package com.andannn.melodify.core.domain.repository

import com.andannn.melodify.core.domain.model.PlayMode
import com.andannn.melodify.core.domain.model.UserSetting
import kotlinx.coroutines.flow.Flow

interface SmpPreferenceRepository {
    val userData: Flow<UserSetting>

    val playMode: Flow<PlayMode>

    val isShuffle: Flow<Boolean>

    suspend fun setPlayMode(playMode: PlayMode)

    suspend fun setIsShuffle(isShuffle: Boolean)
}
