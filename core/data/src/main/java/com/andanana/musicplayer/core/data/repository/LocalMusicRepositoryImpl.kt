package com.andanana.musicplayer.core.data.repository

import android.app.Application
import android.content.ContentUris
import android.provider.MediaStore
import android.util.Log
import com.andanana.musicplayer.core.data.util.CrQueryParameter
import com.andanana.musicplayer.core.data.util.CrQueryUtil
import com.andanana.musicplayer.core.model.MusicItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalMusicRepositoryImpl @Inject constructor(
    private val app: Application
) : LocalMusicRepository {

    override suspend fun getLocalMusicItems() = withContext(Dispatchers.IO) {
        val params = CrQueryParameter()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        params.projection = listOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DURATION
        ).toTypedArray()

        CrQueryUtil.query(app, uri, params)?.use { cursor ->
            val itemList = mutableListOf<MusicItem>()

            val idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val durationIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
            Log.d("NEU", "getLocalMusicItems: ${cursor.count}")
            while (cursor.moveToNext()) {
                itemList.add(
                    MusicItem(
                        contentUri = ContentUris.withAppendedId(uri, cursor.getLong(idIndex)),
                        duration = cursor.getInt(durationIndex)
                    )
                )
            }

            itemList
        } ?: emptyList()
    }
}
