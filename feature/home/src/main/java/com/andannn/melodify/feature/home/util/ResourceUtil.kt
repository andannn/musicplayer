package com.andannn.melodify.feature.home.util

import com.andannn.melodify.feature.home.MediaCategory
import com.andannn.melodify.feature.home.R

object ResourceUtil {
    fun getCategoryResource(category: MediaCategory): Int {
        return when (category) {
            MediaCategory.ALL_MUSIC -> R.string.audio_page_title
            MediaCategory.ALBUM -> R.string.album_page_title
            MediaCategory.ARTIST -> R.string.artist_page_title
        }
    }
}
