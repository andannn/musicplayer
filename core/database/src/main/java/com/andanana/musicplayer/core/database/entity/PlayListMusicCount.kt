package com.andanana.musicplayer.core.database.entity

import androidx.room.Entity

@Entity
data class PlayListMusicCount(val playListId: Long, val musicCount: Int)