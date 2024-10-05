package com.andannn.melodify.core.data.model

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
