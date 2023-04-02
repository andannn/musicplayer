package com.andanana.musicplayer.core.model

enum class PlayMode {
    REPEAT_ONE,
    REPEAT_OFF,
    REPEAT_ALL,
    SHUFFLE;

    companion object {
        val DefaultPlayMode = REPEAT_ALL
    }
}
