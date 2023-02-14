package com.andanana.musicplayer.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "play_list")
data class PlayList(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val cratedDate: Int
)
