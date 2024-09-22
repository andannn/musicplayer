package com.andannn.melodify.feature.home.util

import com.andannn.melodify.core.domain.model.ALBUM_ID
import com.andannn.melodify.core.domain.model.ALL_MUSIC_ID
import com.andannn.melodify.core.domain.model.ARTIST_ID
import com.andannn.melodify.core.domain.model.MINE_PLAYLIST_ID
import com.andannn.melodify.feature.home.R

object ResourceUtil {
    fun getCategoryResource(categoryId: String): Int {
        return when (categoryId) {
            ALL_MUSIC_ID -> R.string.audio_page_title
            ALBUM_ID -> R.string.album_page_title
            ARTIST_ID -> R.string.artist_page_title
            MINE_PLAYLIST_ID -> R.string.play_list_page_title
            else -> R.string.artist_page_title
        }
    }
}
