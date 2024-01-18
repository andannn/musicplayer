package com.andanana.musicplayer.core.data.data

import android.app.Application
import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import com.andanana.musicplayer.core.data.model.AlbumData
import com.andanana.musicplayer.core.data.model.ArtistData
import com.andanana.musicplayer.core.data.model.AudioData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaStoreSourceImpl
    @Inject
    constructor(
        private val app: Application,
    ) : MediaStoreSource {
        override suspend fun getAllMusicData() =
            app.contentResolver.query2(
                uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            )?.use { cursor ->
                parseMusicInfoCursor(cursor)
            } ?: emptyList()

        override suspend fun getAllAlbumData() =
            app.contentResolver.query2(
                uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            )?.use { cursor ->
                parseAlbumInfoCursor(cursor)
            } ?: emptyList()

        override suspend fun getAllArtistData() =
            app.contentResolver.query2(
                uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
            )?.use { cursor ->
                parseArtistInfoCursor(cursor)
            } ?: emptyList()

        override suspend fun getArtistById(id: Long): ArtistData? {
            TODO("Not yet implemented")
        }

        override suspend fun getAlbumById(id: Long): AlbumData =
            app.contentResolver.query2(
                uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                selection = "${MediaStore.Audio.Media._ID} = ?",
                selectionArgs = listOf(id.toString()).toTypedArray(),
            )?.use { cursor ->
                parseAlbumInfoCursor(cursor)
            }?.first() ?: throw IllegalArgumentException("id invalid $id")

        override suspend fun getAudioInAlbum(id: Long) =
            app.contentResolver.query2(
                uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                selection = "${MediaStore.Audio.Media.ALBUM_ID} = ?",
                selectionArgs = listOf(id.toString()).toTypedArray(),
            )?.use { cursor ->
                parseMusicInfoCursor(cursor)
            } ?: emptyList()

        private fun parseMusicInfoCursor(cursor: Cursor): List<AudioData> {
            val itemList = mutableListOf<AudioData>()

            val idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val durationIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
            val dateModifiedIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED)
            val sizeIndex = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)
            val mimeTypeIndex = cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE)
            val albumIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
            val artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val albumIdIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            val artistIdIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)
            val cdTrackNumberIndex = cursor.getColumnIndex(MediaStore.Audio.Media.CD_TRACK_NUMBER)
            val discNumberIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DISC_NUMBER)
            while (cursor.moveToNext()) {
                itemList.add(
                    AudioData(
                        id = cursor.getLong(idIndex),
                        title = cursor.getString(titleIndex),
                        duration = cursor.getInt(durationIndex),
                        modifiedDate = cursor.getLong(dateModifiedIndex),
                        size = cursor.getInt(sizeIndex),
                        mimeType = cursor.getString(mimeTypeIndex),
                        album = cursor.getString(albumIndex),
                        albumId = cursor.getLong(albumIdIndex),
                        artist = cursor.getString(artistIndex),
                        artistId = cursor.getLong(artistIdIndex),
                        cdTrackNumber = cursor.getInt(cdTrackNumberIndex),
                        discNumberIndex = cursor.getInt(discNumberIndex),
                    ),
                )
            }
            return itemList
        }

        private fun parseArtistInfoCursor(cursor: Cursor): List<ArtistData> {
            val itemList = mutableListOf<ArtistData>()

            val idIndex = cursor.getColumnIndex(MediaStore.Audio.Artists._ID)
            val artistIndex = cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)
            val numberOfTracksIndex = cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS)
            while (cursor.moveToNext()) {
                itemList.add(
                    ArtistData(
                        artistId = cursor.getLong(idIndex),
                        name = cursor.getString(artistIndex),
                        artistCoverUri = Uri.parse(""),
                        trackCount = cursor.getInt(numberOfTracksIndex),
                    ),
                )
            }
            return itemList
        }

        private fun parseAlbumInfoCursor(cursor: Cursor): List<AlbumData> {
            val itemList = mutableListOf<AlbumData>()

            val idIndex = cursor.getColumnIndex(MediaStore.Audio.Albums._ID)
            val albumIndex = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)
            val numberOfSongsIndex = cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS)
            while (cursor.moveToNext()) {
                itemList.add(
                    AlbumData(
                        albumId = cursor.getLong(idIndex),
                        title = cursor.getString(albumIndex),
                        trackCount = cursor.getInt(numberOfSongsIndex),
                    ),
                )
            }
            return itemList
        }
    }

suspend fun ContentResolver.query2(
    uri: Uri,
    projection: Array<String>? = null,
    selection: String? = null,
    selectionArgs: Array<String>? = null,
    order: String = MediaStore.MediaColumns._ID,
    ascending: Boolean = true,
    offset: Int = 0,
    limit: Int = Int.MAX_VALUE,
): Cursor? {
    return withContext(Dispatchers.Default) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val args2 =
                Bundle().apply {
                    // Limit & Offset
                    putInt(ContentResolver.QUERY_ARG_LIMIT, limit)
                    putInt(ContentResolver.QUERY_ARG_OFFSET, offset)

                    // order
                    putStringArray(ContentResolver.QUERY_ARG_SORT_COLUMNS, arrayOf(order))
                    putInt(
                        ContentResolver.QUERY_ARG_SORT_DIRECTION,
                        if (ascending) ContentResolver.QUERY_SORT_DIRECTION_ASCENDING else ContentResolver.QUERY_SORT_DIRECTION_DESCENDING,
                    )
                    // Selection and groupBy
                    if (selectionArgs != null) {
                        putStringArray(
                            ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS,
                            selectionArgs,
                        )
                    }
                    if (selection != null) {
                        putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection)
                    }
                }
            query(uri, projection, args2, null)
        } else {
            val order2 =
                order + (if (ascending) " ASC" else " DESC") + " LIMIT $limit OFFSET $offset"
            query(uri, projection, selection, selectionArgs, order2)
        }
    }
}
