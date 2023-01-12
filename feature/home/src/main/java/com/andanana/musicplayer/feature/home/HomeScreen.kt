package com.andanana.musicplayer.feature.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreen()
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun HomeScreen() {
    val state = rememberPagerState(1)

    HorizontalPager(
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
