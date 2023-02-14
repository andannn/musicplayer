package com.andanana.musicplayer.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music")
data class Music(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val addDate: Int
)
