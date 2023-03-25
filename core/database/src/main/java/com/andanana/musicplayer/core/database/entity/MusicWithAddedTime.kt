package com.andanana.musicplayer.core.database.entity

import androidx.room.Entity

@Entity
data class MusicWithAddedTime(
    val music: Music,
    val musicAddedDate: Long
)
