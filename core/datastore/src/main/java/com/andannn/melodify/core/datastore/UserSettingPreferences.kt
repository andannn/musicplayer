package com.andannn.melodify.core.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserSettingPreferences
@Inject
constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userDate =
        userPreferences.data
            .map {
                UserSettingPref(
                    playMode = it.playModeValue,
                    isShuffle = it.isShuffle,
                    mediaPreviewMode = it.mediaPreviewModeValue
                )
            }


    suspend fun setPlayMode(playMode: Int) {
        userPreferences.updateData { userPreferences ->
            userPreferences.copy {
                this.playMode = PlayModeProto.forNumber(playMode)
            }
        }
    }

    suspend fun setIsShuffle(isShuffle: Boolean) {
        userPreferences.updateData { userPreferences ->
            userPreferences.copy {
                this.isShuffle = isShuffle
            }
        }
    }

    suspend fun setMediaPreviewMode(mediaPreviewMode: Int) {
        userPreferences.updateData { userPreferences ->
            userPreferences.copy {
                this.mediaPreviewMode = MediaPreviewMode.forNumber(mediaPreviewMode)
            }
        }
    }
}