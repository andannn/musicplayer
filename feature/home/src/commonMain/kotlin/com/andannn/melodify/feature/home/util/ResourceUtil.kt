package com.andannn.melodify.feature.home.util

import com.andannn.melodify.core.data.model.CustomTab
import melodify.feature.common.generated.resources.Res
import melodify.feature.common.generated.resources.album_page_title
import melodify.feature.common.generated.resources.artist_page_title
import melodify.feature.common.generated.resources.audio_page_title
import melodify.feature.common.generated.resources.genre_title
import org.jetbrains.compose.resources.StringResource

object ResourceUtil {
    fun getCategoryResource(category: CustomTab): StringResource {
        return when (category) {
            is CustomTab.AlbumDetail -> TODO()
            CustomTab.AllAlbum -> Res.string.album_page_title
            CustomTab.AllArtist -> Res.string.artist_page_title
            CustomTab.AllGenre -> Res.string.genre_title
            CustomTab.AllMusic -> Res.string.audio_page_title
            is CustomTab.ArtistDetail -> TODO()
            is CustomTab.GenreDetail -> TODO()
        }
    }
}
