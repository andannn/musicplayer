package com.andanana.musicplayer.core.data.repository

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.os.CancellationSignal
import android.util.Size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AlbumArtRepositoryImpl @Inject constructor(
    private val application: Application
) : AlbumArtRepository {

    private val imageMap: MutableMap<Uri, Bitmap> = mutableMapOf()

    override suspend fun loadAlbumArt(contentUri: Uri, size: Size): ImageResult {
        if (imageMap.containsKey(contentUri)) return ImageResult.Success(imageMap[contentUri]!!)

        return try {
            val bitmap = withContext(Dispatchers.IO) {
                loadThumbnail(contentUri, size)
            }
            imageMap[contentUri] = bitmap
            ImageResult.Success(bitmap)
        } catch (e: IOException) {
            ImageResult.Error
        }
    }

    private suspend fun loadThumbnail(contentUri: Uri, size: Size) =
        suspendCancellableCoroutine<Bitmap> { continuation ->
            val cancellationSignal = CancellationSignal()

            continuation.invokeOnCancellation {
                cancellationSignal.cancel()
            }

            try {
                val bitmap = application.contentResolver.loadThumbnail(
                    /* uri = */ contentUri,
                    /* size = */size,
                    /* signal = */cancellationSignal
                )
                continuation.resume(bitmap)
            } catch (e: IOException) {
                continuation.resumeWithException(e)
            }
        }
}
