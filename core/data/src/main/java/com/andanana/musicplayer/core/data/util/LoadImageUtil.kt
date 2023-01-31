package com.andanana.musicplayer.core.data.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.util.Size
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay

private const val TAG = "LoadImageUtil"

suspend fun loadImage(
    context: Context,
    contentUri: Uri
): Bitmap {
    try {
    } catch (e: CancellationException) {
        Log.d(TAG, "MusicCard: CancellationException loadImage")
    }
    Log.d(TAG, "loadImage: ${Thread.currentThread().name}")
    return context.contentResolver.loadThumbnail(
        /* uri = */ contentUri,
        Size(300, 300),
        null
    )
}
