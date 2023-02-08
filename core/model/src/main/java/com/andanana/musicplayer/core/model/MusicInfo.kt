package com.andanana.musicplayer.core.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import androidx.media3.common.MediaItem

data class MusicInfo(
    val contentUri: Uri,
    val title: String = "",
    val duration: Int = 0,
    val modifiedDate: Long = 0,
    val size: Int = 0,
    val mimeType: String = "",
    val absolutePath: String = "",
    val album: String = "",
    val artist: String = "",
    val albumUri: Uri = Uri.parse("")
) : Parcelable {

    val mediaItem: MediaItem = MediaItem.fromUri(contentUri)

    constructor(parcel: Parcel) : this(
        contentUri = parcel.readParcelable(Uri::class.java.classLoader) ?: Uri.EMPTY,
        title = parcel.readString() ?: "",
        duration = parcel.readInt(),
        modifiedDate = parcel.readLong(),
        size = parcel.readInt(),
        mimeType = parcel.readString() ?: "",
        absolutePath = parcel.readString() ?: "",
        album = parcel.readString() ?: "",
        artist = parcel.readString() ?: "",
        albumUri = parcel.readParcelable(Uri::class.java.classLoader) ?: Uri.EMPTY
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(contentUri, flags)
        parcel.writeString(title)
        parcel.writeInt(duration)
        parcel.writeLong(modifiedDate)
        parcel.writeInt(size)
        parcel.writeString(mimeType)
        parcel.writeString(absolutePath)
        parcel.writeString(album)
        parcel.writeString(artist)
        parcel.writeParcelable(albumUri, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MusicInfo> {
        override fun createFromParcel(parcel: Parcel): MusicInfo {
            return MusicInfo(parcel)
        }

        override fun newArray(size: Int): Array<MusicInfo?> {
            return arrayOfNulls(size)
        }
    }
}
