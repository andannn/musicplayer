package com.andannn.melodify.core.domain.model

data class UserSetting(
    val playMode: PlayMode,
    val isShuffle: Boolean,
    val mediaPreviewMode: MediaPreviewMode
)
