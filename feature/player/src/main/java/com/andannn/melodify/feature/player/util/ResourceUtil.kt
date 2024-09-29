package com.andannn.melodify.feature.player.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.RepeatOn
import androidx.compose.material.icons.rounded.RepeatOneOn
import com.andannn.melodify.core.domain.model.PlayMode
import com.andannn.melodify.common.R
import com.andannn.melodify.feature.player.SheetTab

fun PlayMode.getIcon() =
    when (this) {
        PlayMode.REPEAT_ONE -> Icons.Rounded.RepeatOneOn
        PlayMode.REPEAT_OFF -> Icons.Rounded.Repeat
        PlayMode.REPEAT_ALL -> Icons.Rounded.RepeatOn
    }

fun SheetTab.getLabel() = when(this) {
    SheetTab.NEXT_SONG -> R.string.play_queue
    SheetTab.LYRICS -> R.string.lyrics
}