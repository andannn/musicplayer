package com.andannn.melodify.core.data.repository

import com.andannn.melodify.core.datastore.UserSettingPreferences
import com.andannn.melodify.core.datastore.model.PlatModeValues
import com.andannn.melodify.core.datastore.model.PreviewModeValues
import com.andannn.melodify.core.data.model.MediaPreviewMode
import com.andannn.melodify.core.data.model.PlayMode
import com.andannn.melodify.core.data.model.UserSetting
import com.andannn.melodify.core.data.UserPreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class UserPreferenceRepositoryImpl(
    private val preferences: UserSettingPreferences,
) : UserPreferenceRepository {
    override val userSettingFlow: Flow<UserSetting> = preferences.userDate.map {
        UserSetting(
            playMode = it.playMode.toPlayMode(),
            isShuffle = it.isShuffle,
            mediaPreviewMode = it.mediaPreviewMode.toMediaPreviewMode()
        )
    }

    override val playMode: Flow<PlayMode> =
        userSettingFlow
            .map { it.playMode }
            .distinctUntilChanged()

    override val isShuffle: Flow<Boolean> =
        userSettingFlow
            .map { it.isShuffle }
            .distinctUntilChanged()

    override val previewMode: Flow<MediaPreviewMode> =
        userSettingFlow
            .map { it.mediaPreviewMode }
            .distinctUntilChanged()

    override suspend fun setPlayMode(playMode: PlayMode) {
        preferences.setPlayMode(playMode.toIntValue())
    }

    override suspend fun setIsShuffle(isShuffle: Boolean) {
        preferences.setIsShuffle(isShuffle)
    }

    override suspend fun setPreviewMode(previewMode: MediaPreviewMode) {
        preferences.setMediaPreviewMode(previewMode.toIntValue())
    }
}

private fun MediaPreviewMode.toIntValue(): Int = when (this) {
    MediaPreviewMode.LIST_PREVIEW -> PreviewModeValues.LIST_PREVIEW_VALUE
    MediaPreviewMode.GRID_PREVIEW -> PreviewModeValues.GRID_PREVIEW_VALUE
}

private fun PlayMode.toIntValue(): Int = when (this) {
    PlayMode.REPEAT_ONE -> PlatModeValues.PLAT_MODE_REPEAT_ONE_VALUE
    PlayMode.REPEAT_ALL -> PlatModeValues.PLAT_MODE_REPEAT_ALL_VALUE
    PlayMode.REPEAT_OFF -> PlatModeValues.PLAT_MODE_REPEAT_OFF_VALUE
}


private fun Int.toPlayMode(): PlayMode = when (this) {
    PlatModeValues.PLAT_MODE_REPEAT_ONE_VALUE -> PlayMode.REPEAT_ONE
    PlatModeValues.PLAT_MODE_REPEAT_ALL_VALUE -> PlayMode.REPEAT_ALL
    PlatModeValues.PLAT_MODE_REPEAT_OFF_VALUE -> PlayMode.REPEAT_OFF

    // Default
    else -> PlayMode.REPEAT_OFF
}

private fun Int.toMediaPreviewMode(): MediaPreviewMode = when (this) {
    PreviewModeValues.LIST_PREVIEW_VALUE -> MediaPreviewMode.LIST_PREVIEW
    PreviewModeValues.GRID_PREVIEW_VALUE -> MediaPreviewMode.GRID_PREVIEW

    // Default
    else -> MediaPreviewMode.GRID_PREVIEW
}

