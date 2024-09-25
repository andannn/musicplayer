package com.andannn.melodify.core.data.repository

import com.andannn.melodify.core.datastore.SmpPreferencesDataSource
import com.andannn.melodify.core.domain.model.PlayMode
import com.andannn.melodify.core.domain.model.UserSetting
import com.andannn.melodify.core.domain.repository.SmpPreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SmpPreferenceRepositoryImpl
@Inject
constructor(
    private val dataSource: SmpPreferencesDataSource,
) : SmpPreferenceRepository {
    override val userData: Flow<UserSetting> = dataSource.userDate

    override val playMode: Flow<PlayMode> =
        userData
            .map { it.playMode }
            .distinctUntilChanged()

    override val isShuffle: Flow<Boolean> =
        userData
            .map { it.isShuffle }
            .distinctUntilChanged()

    override suspend fun setPlayMode(playMode: PlayMode) {
        dataSource.setPlayMode(playMode)
    }

    override suspend fun setIsShuffle(isShuffle: Boolean) {
        dataSource.setIsShuffle(isShuffle)
    }
}
