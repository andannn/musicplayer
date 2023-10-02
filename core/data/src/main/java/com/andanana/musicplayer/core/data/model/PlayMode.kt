package com.andanana.musicplayer.core.data.model

enum class PlayMode {
    REPEAT_ONE,
    REPEAT_OFF,
    REPEAT_ALL,
    SHUFFLE;

    fun next(): PlayMode {
        val nextIndex = (this.ordinal + 1) % PlayMode.values().size
        return PlayMode.values()[nextIndex]
    }

    companion object {
        val DefaultPlayMode = REPEAT_ALL
    }
}
