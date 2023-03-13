package com.andanana.musicplayer.feature.library

import android.os.Parcel
import android.os.Parcelable
import com.andanana.musicplayer.core.database.entity.PlayList

data class PlayListItem(
    val id: Long,
    val name: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlayListItem> {
        override fun createFromParcel(parcel: Parcel): PlayListItem {
            return PlayListItem(parcel)
        }

        override fun newArray(size: Int): Array<PlayListItem?> {
            return arrayOfNulls(size)
        }
    }
}

fun PlayList.matToUiData() = PlayListItem(id = this.playListId, this.name)
fun List<PlayList>.matToUiData() = this.map {
    it.matToUiData()
}
