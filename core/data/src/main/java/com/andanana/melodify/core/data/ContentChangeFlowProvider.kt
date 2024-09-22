package com.andanana.melodify.core.data

import android.content.Context
import android.database.ContentObserver
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

private const val TAG = "Syncer"

class ContentChangeFlowProvider
    @Inject
    constructor(
        @ApplicationContext context: Context,
    ) {
        private val contentResolver = context.contentResolver

        val audioChangedEventFlow =
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
    }
