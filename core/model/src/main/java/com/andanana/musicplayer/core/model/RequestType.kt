package com.andanana.musicplayer.core.model

import android.net.Uri
import android.provider.MediaStore

const val PlayListContentUri = "content://m_playlist/"

enum class RequestType(val externalContentUri: String) {
    ALBUM_REQUEST(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI.toString()),
    ARTIST_REQUEST(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI.toString()),
    MUSIC_REQUEST(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString()),
    PLAYLIST_REQUEST(PlayListContentUri);

    companion object {
        fun Uri.toRequestType(): RequestType? {
            return RequestType.values().find {
                this.toString().contains(it.externalContentUri)
            }
        }

        fun RequestType.toUri(lastPathSegment: String): Uri =
            Uri.withAppendedPath(Uri.parse(externalContentUri), lastPathSegment)
    }
}
