package com.andannn.melodify.core.domain.model

enum class MediaPreviewMode {
    GRID_PREVIEW,
    LIST_PREVIEW;

    fun next(): MediaPreviewMode = when(this) {
        GRID_PREVIEW -> LIST_PREVIEW
        LIST_PREVIEW -> GRID_PREVIEW
    }
}
