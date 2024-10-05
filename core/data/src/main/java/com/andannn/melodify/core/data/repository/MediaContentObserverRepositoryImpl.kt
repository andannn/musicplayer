package com.andannn.melodify.core.data.repository

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.andannn.melodify.core.data.MediaContentObserverRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

private const val TAG = "MediaContentObserverRepository"

class MediaContentObserverRepositoryImpl(
    context: Context,
) : MediaContentObserverRepository {
    private val contentResolver = context.contentResolver

    override val allAlbumUri: String
        get() = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI.toString()

    override val allArtistUri: String
        get() = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI.toString()

    override val allAudioUri: String
        get() = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString()

    override fun getAlbumUri(albumId: Long): String {
        return MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI.toString() + "/" + albumId
    }

    override fun getArtistUri(artistId: Long): String {
        return MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI.toString() + "/" + artistId
    }

    override fun getContentChangedEventFlow(uri: String): Flow<Unit> {
        Log.d(TAG, "getContentChangedEventFlow: $uri")
        return callbackFlow {
            val observer =
                object : ContentObserver(null) {
                    override fun onChange(selfChange: Boolean) {
                        trySend(Unit)
                    }
                }

            contentResolver.registerContentObserver(
                /* uri = */ Uri.parse(uri),
                /* notifyForDescendants = */ true,
                /* observer = */ observer,
            )

            trySend(Unit)

            awaitClose {
                contentResolver.unregisterContentObserver(observer)
            }
        }.onStart { emit(Unit) }
    }
}