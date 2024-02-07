package com.andanana.musicplayer.core.model

import androidx.media3.common.Player

enum class PlayMode {
    REPEAT_ONE,
    REPEAT_OFF,
    REPEAT_ALL,
    ;

    fun next(): PlayMode {
        val nextIndex = (this.ordinal + 1) % entries.size
        return entries[nextIndex]
    }

    companion object {
        val DefaultPlayMode = REPEAT_ALL
    }
}

fun PlayMode.toExoPlayerMode() =
    when (this) {
        PlayMode.REPEAT_ONE -> Player.REPEAT_MODE_ONE
        PlayMode.REPEAT_OFF -> Player.REPEAT_MODE_OFF
        PlayMode.REPEAT_ALL -> Player.REPEAT_MODE_ALL
    }
