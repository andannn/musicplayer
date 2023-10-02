package com.andanana.musicplayer.core.datastore.repository

import com.andanana.musicplayer.core.datastore.SmpPreferencesDataSource
import com.andanana.musicplayer.core.data.model.PlayMode
import com.andanana.musicplayer.core.data.model.UserSetting
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SmpPreferenceRepositoryImpl @Inject constructor(
    private val dataSource: SmpPreferencesDataSource
): SmpPreferenceRepository {
    override val userData: Flow<UserSetting> = dataSource.userDate

    override suspend fun setPlayMode(playMode: PlayMode) {
        dataSource.setPlayMode(playMode)
    }
}