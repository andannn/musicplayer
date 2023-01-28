package com.andanana.musicplayer.feature.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier
) {
    HomeScreen(modifier = modifier)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier
) {
    val state = rememberPagerState()

    HorizontalPager(
        modifier = modifier,
        count = HomePage.values().size,
        state = state
    ) { pageIndex ->
        when (HomePage.values()[pageIndex]) {
            HomePage.AUDIO_PAGE -> AudioPage(Modifier.fillMaxSize())
            HomePage.ALBUM_PAGE -> AlbumPage(Modifier.fillMaxSize())
            HomePage.ARTIST_PAGE -> ArtistPage(Modifier.fillMaxSize())
        }
    }
}

enum class HomePage {
    AUDIO_PAGE,
    ALBUM_PAGE,
    ARTIST_PAGE
}
