package com.andanana.musicplayer.feature.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

private const val TAG = "HomeScreen"

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
    val scope = rememberCoroutineScope()
    val pageState = rememberPagerState()
    var selectedPageIndex by remember { mutableStateOf(pageState.currentPage) }
    val homePage = remember(selectedPageIndex) { HomePage.values()[selectedPageIndex] }

    LaunchedEffect(Unit) {
        snapshotFlow {
            pageState.currentPage
        }.collect { index ->
            selectedPageIndex = index
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = selectedPageIndex
        ) {
            HomePage.values().forEach { page ->
                Tab(
                    selected = page == homePage,
                    onClick = {
                        scope.launch {
                            pageState.animateScrollToPage(page.ordinal)
                        }
                    }
                ) {
                    Text(
                        modifier = modifier.padding(vertical = 10.dp),
                        text = stringResource(id = page.titleResId)
                    )
                }
            }
        }

        HorizontalPager(
            count = HomePage.values().size,
            state = pageState
        ) { index ->
            when (HomePage.values()[index]) {
                HomePage.AUDIO_PAGE -> AudioPage(Modifier.fillMaxSize())
                HomePage.ALBUM_PAGE -> AlbumPage(Modifier.fillMaxSize())
                HomePage.ARTIST_PAGE -> ArtistPage(Modifier.fillMaxSize())
            }
        }
    }
}

enum class HomePage(
    @StringRes val titleResId: Int
) {
    AUDIO_PAGE(R.string.audio_page_title),
    ALBUM_PAGE(R.string.album_page_title),
    ARTIST_PAGE(R.string.artist_page_title),
}