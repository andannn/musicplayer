package com.andanana.musicplayer.feature.library

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.andanana.musicplayer.core.database.entity.PlayList
import com.andanana.musicplayer.core.model.PlayListContentUri

data class PlayListItem(
    val id: Long,
    val name: String,
    val count: Int = 0,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readInt(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeInt(count)
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

fun PlayList.matToUiData() = PlayListItem(id = this.playListId, this.name, 0)
fun List<PlayList>.matToUiData() = this.map {
    it.matToUiData()
}

fun PlayListItem.toUri(): Uri {
    return Uri.parse("$PlayListContentUri${this.id}")
}
