package com.andannn.melodify.common.drawer

import com.andannn.melodify.common.R
import com.andannn.melodify.core.designsystem.icons.SmpIcon
import com.andannn.melodify.core.designsystem.icons.SimpleMusicIcons

enum class SheetOptionItem(
    val smpIcon: SmpIcon,
    val text: Int,
) {
    PLAY_NEXT(
        smpIcon = SimpleMusicIcons.PlayNext,
        text = R.string.play_next,
    ),
    DELETE(
        smpIcon = SimpleMusicIcons.Delete,
        text = R.string.delete,
    ),
    ADD_TO_QUEUE(
        smpIcon = SimpleMusicIcons.Delete,
        text = R.string.add_to_queue,
    ),
    SLEEP_TIMER(
        smpIcon = SimpleMusicIcons.Timer,
        text = R.string.add_to_queue,
    ),
}
