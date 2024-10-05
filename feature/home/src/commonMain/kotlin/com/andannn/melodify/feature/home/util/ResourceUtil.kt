package com.andannn.melodify.feature.home.util

import com.andannn.melodify.feature.home.MediaCategory
import melodify.feature.common.generated.resources.Res
import melodify.feature.common.generated.resources.album_page_title
import melodify.feature.common.generated.resources.artist_page_title
import melodify.feature.common.generated.resources.audio_page_title
import org.jetbrains.compose.resources.StringResource

object ResourceUtil {
    fun getCategoryResource(category: MediaCategory): StringResource {
        return when (category) {
            MediaCategory.ALL_MUSIC -> Res.string.audio_page_title
            MediaCategory.ALBUM -> Res.string.album_page_title
            MediaCategory.ARTIST -> Res.string.artist_page_title
        }
    }
}
