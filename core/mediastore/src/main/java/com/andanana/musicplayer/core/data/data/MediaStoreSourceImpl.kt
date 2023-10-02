package com.andanana.musicplayer.core.data.data

import android.app.Application
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.andanana.musicplayer.core.data.model.AlbumData
import com.andanana.musicplayer.core.data.model.ArtistData
import com.andanana.musicplayer.core.data.model.AudioData
import com.andanana.musicplayer.core.data.util.CrQueryParameter
import com.andanana.musicplayer.core.data.util.CrQueryUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "MediaStoreSourceImpl"

private val musicInfoProjection = listOf(
    MediaStore.Audio.Media._ID,
    MediaStore.Audio.Media.TITLE,
    MediaStore.Audio.Media.DURATION,
    MediaStore.Audio.Media.DATE_MODIFIED,
    MediaStore.Audio.Media.SIZE,
    MediaStore.Audio.Media.MIME_TYPE,
    MediaStore.Audio.Media.ALBUM,
    MediaStore.Audio.Media.ARTIST,
    MediaStore.Audio.Media.ALBUM_ID,
    MediaStore.Audio.Media.ARTIST_ID,
    MediaStore.Audio.Media.CD_TRACK_NUMBER,
    MediaStore.Audio.Media.DISC_NUMBER
).toTypedArray()

private val artistInfoProjection = listOf(
    MediaStore.Audio.Artists._ID,
    MediaStore.Audio.Artists.ARTIST,
    MediaStore.Audio.Artists.NUMBER_OF_TRACKS
).toTypedArray()

private val albumInfoProjection = listOf(
    MediaStore.Audio.Albums._ID,
    MediaStore.Audio.Albums.ALBUM,
    MediaStore.Audio.Albums.NUMBER_OF_SONGS,
    MediaStore.Audio.Albums.ARTIST,
).toTypedArray()

// private const val MimeTypeLimitation = "(${MediaStore.Audio.Media.MIME_TYPE} in (?,?,?))"
// private val MimeTypeSelectionArg = listOf(
//    "audio/x-wav",
//    "audio/mpeg",
//    "audio/flac"
// ).toTypedArray()

@Singleton
class MediaStoreSourceImpl @Inject constructor(
    private val app: Application
) : MediaStoreSource {
    override suspend fun getAllMusicData() = withContext(Dispatchers.IO) {
        queryAudio {
            projection = musicInfoProjection
//            where = MimeTypeLimitation
//            selectionArgs = MimeTypeSelectionArg
        }
    }

    override suspend fun getAllAlbumData() = withContext(Dispatchers.IO) {
        queryAlbumInfo {
            projection = albumInfoProjection
        }
    }

    override suspend fun getAllArtistData() = withContext(Dispatchers.IO) {
        queryArtistInfo {
            projection = artistInfoProjection
        }
    }

    private fun queryAudio(paramAdjuster: CrQueryParameter.() -> Unit): List<AudioData> {
        val params = CrQueryParameter()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        params.paramAdjuster()
        return CrQueryUtil.query(app, uri, params)?.use { cursor ->
            parseMusicInfoCursor(cursor)
        } ?: emptyList()
    }

    private fun queryArtistInfo(paramAdjuster: CrQueryParameter.() -> Unit): List<ArtistData> {
        val params = CrQueryParameter()
        val uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI
        params.paramAdjuster()
        return CrQueryUtil.query(app, uri, params)?.use { cursor ->
            parseArtistInfoCursor(cursor)
        } ?: emptyList()
    }

    private fun queryAlbumInfo(paramAdjuster: CrQueryParameter.() -> Unit): List<AlbumData> {
        val params = CrQueryParameter()
        val uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
        params.paramAdjuster()
        return CrQueryUtil.query(app, uri, params)?.use { cursor ->
            parseAlbumInfoCursor(cursor)
        } ?: emptyList()
    }

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
                    discNumberIndex = cursor.getInt(discNumberIndex)
                )
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
                    artistCoverUri = getArtistCoverUriByName(cursor.getString(artistIndex))
                        ?: Uri.parse(""),
                    trackCount = cursor.getInt(numberOfTracksIndex)
                )
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
                    trackCount = cursor.getInt(numberOfSongsIndex)
                )
            )
        }
        return itemList
    }

    private fun getArtistCoverUriByName(name: String): Uri? {
        val params = CrQueryParameter()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        params.apply {
            projection = listOf(MediaStore.Audio.Media.ALBUM_ID).toTypedArray()
            where = "(${MediaStore.Audio.Media.ARTIST} like ?)"
            selectionArgs = listOf(name).toTypedArray()
            limit = 1
        }
        return CrQueryUtil.query(app, uri, params)?.use { cursor ->
            if (cursor.count >= 1) {
                cursor.moveToFirst()
                val albumIdIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
                Uri.withAppendedPath(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    cursor.getLong(albumIdIndex).toString()
                )
            } else {
                null
            }
        }
    }
}
