package com.andannn.melodify.core.domain.repository

import com.andannn.melodify.core.domain.model.MediaPreviewMode
import com.andannn.melodify.core.domain.model.PlayMode
import com.andannn.melodify.core.domain.model.UserSetting
import kotlinx.coroutines.flow.Flow

interface UserPreferenceRepository {
    val userSettingFlow: Flow<UserSetting>

    val playMode: Flow<PlayMode>

    val isShuffle: Flow<Boolean>

    val previewMode: Flow<MediaPreviewMode>

    suspend fun setPlayMode(playMode: PlayMode)

    suspend fun setIsShuffle(isShuffle: Boolean)

    suspend fun setPreviewMode(previewMode: MediaPreviewMode)
}
