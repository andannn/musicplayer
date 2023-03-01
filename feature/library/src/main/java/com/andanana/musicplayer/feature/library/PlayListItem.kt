package com.andanana.musicplayer.feature.library

import com.andanana.musicplayer.core.database.entity.PlayList

data class PlayListItem(
    val id: Long,
    val name: String
)

fun PlayList.matToUiData() = PlayListItem(id = this.playListId, this.name)
fun List<PlayList>.matToUiData() = this.map {
    it.matToUiData()
}
