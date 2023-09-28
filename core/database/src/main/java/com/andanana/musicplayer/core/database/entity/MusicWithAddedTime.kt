package com.andanana.musicplayer.core.database.entity

import androidx.room.Entity

@Entity
data class MusicWithAddedTime(
    val musicEntity: MusicEntity,
    val musicAddedDate: Long
)
