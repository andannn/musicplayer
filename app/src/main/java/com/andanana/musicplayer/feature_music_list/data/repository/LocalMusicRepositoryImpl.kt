package com.andanana.musicplayer.feature_music_list.data.repository

import android.app.Application
import android.content.ContentUris
import android.provider.MediaStore
import android.util.Log
import com.andanana.musicplayer.feature_music_list.domain.model.MusicItem
import com.andanana.musicplayer.feature_music_list.domain.repository.LocalMusicRepository
import com.andanana.musicplayer.feature_music_list.domain.util.CrQueryParameter
import com.andanana.musicplayer.feature_music_list.domain.util.CrQueryUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalMusicRepositoryImpl(
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
