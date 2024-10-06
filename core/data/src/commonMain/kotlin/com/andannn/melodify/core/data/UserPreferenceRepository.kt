package com.andannn.melodify.core.data

import com.andannn.melodify.core.data.model.MediaPreviewMode
import com.andannn.melodify.core.data.model.PlayMode
import com.andannn.melodify.core.data.model.UserSetting
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
