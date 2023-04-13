package com.andanana.musicplayer.feature.player.util

import com.andanana.musicplayer.core.model.PlayMode
import com.andanana.musicplayer.feature.player.R

fun PlayMode.getIconRes() = when (this) {
    PlayMode.REPEAT_ONE -> R.drawable.repeate_one_icn
    PlayMode.REPEAT_OFF -> R.drawable.playmode_repeat_off
    PlayMode.REPEAT_ALL -> R.drawable.playmode_loop_icn
    PlayMode.SHUFFLE -> R.drawable.playmode_random_icn
}