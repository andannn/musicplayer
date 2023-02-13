package com.andanana.musicplayer.core.model

import android.net.Uri
import android.provider.MediaStore
import com.andanana.musicplayer.core.designsystem.Drawer

enum class RequestType(val externalContentUri: String) {
    ALBUM_REQUEST(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI.toString()),
    ARTIST_REQUEST(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI.toString()),
    MUSIC_REQUEST(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString());

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

fun RequestType.toDrawer(): Drawer {
    return when (this) {
        RequestType.MUSIC_REQUEST -> Drawer.MusicDrawer
        RequestType.ALBUM_REQUEST -> Drawer.AlbumDrawer
        RequestType.ARTIST_REQUEST -> Drawer.ArtistDrawer
        else -> error("Invalid Type")
    }
}
