package com.andanana.musicplayer.core.datastore

import androidx.datastore.core.DataStore
import com.andanana.musicplayer.core.model.PlayMode
import com.andanana.musicplayer.core.model.UserSetting
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SmpPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    val userDate = userPreferences.data
        .map {
            UserSetting(
                playMode = when (it.playMode) {
                    PlayModeProto.PLAT_MODE_REPEAT_ONE -> PlayMode.REPEAT_ONE
                    PlayModeProto.PLAT_MODE_REPEAT_OFF -> PlayMode.REPEAT_OFF
                    PlayModeProto.PLAT_MODE_SHUFFLE -> PlayMode.SHUFFLE
                    PlayModeProto.PLAT_MODE_REPEAT_ALL -> PlayMode.REPEAT_ALL
                    else -> PlayMode.DefaultPlayMode // Default
                }
            )
        }

    suspend fun setPlayMode(playMode: PlayMode) {
        userPreferences.updateData { userPreferences ->
            userPreferences.copy {
                this.playMode = when (playMode) {
                    PlayMode.REPEAT_ONE -> PlayModeProto.PLAT_MODE_REPEAT_ONE
                    PlayMode.REPEAT_OFF -> PlayModeProto.PLAT_MODE_REPEAT_OFF
                    PlayMode.REPEAT_ALL -> PlayModeProto.PLAT_MODE_REPEAT_ALL
                    PlayMode.SHUFFLE -> PlayModeProto.PLAT_MODE_SHUFFLE
                }
            }
        }
    }
}
