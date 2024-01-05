package com.andanana.musicplayer.core.data

import android.content.Context
import android.database.ContentObserver
import android.provider.MediaStore
import android.util.Log
import com.andanana.musicplayer.core.data.repository.MusicRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

private const val TAG = "Syncer"

interface Syncer {
    val isSyncing: Boolean

    suspend fun observingMediaContent()

    fun audioPermissionChanged(hasPermission: Boolean)

    fun observeIsSyncing(): Flow<Boolean>
}

class SyncerImpl
    @Inject
    constructor(
        @ApplicationContext context: Context,
        private val mediaRepository: MusicRepository,
    ) : Syncer {
        private val contentResolver = context.contentResolver

        private val audioChangedEventFlow =
            callbackFlow {
                val observer =
                    object : ContentObserver(null) {
                        override fun onChange(selfChange: Boolean) {
                            trySend(Unit)
                        }
                    }

                contentResolver.registerContentObserver(
                    // uri =
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    // notifyForDescendants =
                    true,
                    // observer =
                    observer,
                )

                trySend(Unit)

                awaitClose {
                    contentResolver.unregisterContentObserver(observer)
                }
            }

        private val isSyncingState = MutableStateFlow(false)
        private val audioPermissionState = MutableStateFlow(false)

        override val isSyncing: Boolean
            get() = isSyncingState.value

        override fun observeIsSyncing() = isSyncingState

        override fun audioPermissionChanged(hasPermission: Boolean) {
            audioPermissionState.value = hasPermission
        }

        override suspend fun observingMediaContent() {
            combine(
                audioPermissionState,
                audioChangedEventFlow,
            ) { hasPermission, _ -> hasPermission }.collectLatest { hasPermission ->
                if (hasPermission) {
                    Log.d(TAG, "mediaStoreChanged: sync start")
                    isSyncingState.value = true
                    mediaRepository.sync()
                    isSyncingState.value = false
                    Log.d(TAG, "mediaStoreChanged: sync finished")
                }
            }
        }
    }
