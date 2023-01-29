package com.andanana.musicplayer.core.data.repository

import android.graphics.Bitmap
import android.net.Uri
import android.util.Size

sealed interface ImageResult {
    object Loading : ImageResult
    object Error : ImageResult
    data class Success(val bitmap: Bitmap) : ImageResult
}

interface AlbumArtRepository {
    suspend fun loadAlbumArt(contentUri: Uri, size: Size): ImageResult
}