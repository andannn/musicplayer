package com.andanana.musicplayer.feature.home

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.andanana.musicplayer.core.designsystem.component.TabRowAndPager
import com.andanana.musicplayer.core.model.MusicInfo

private const val TAG = "HomeScreen"

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    onPlayMusicInList: (List<MusicInfo>, Int) -> Unit,
    onNavigateToPlayList: (Uri) -> Unit,
    onShowMusicItemOption: (Uri) -> Unit
) {
    HomeScreen(
        modifier = modifier,
        onPlayMusicInList = onPlayMusicInList,
        onNavigateToPlayList = onNavigateToPlayList,
        onShowMusicItemOption = onShowMusicItemOption
    )
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    onPlayMusicInList: (List<MusicInfo>, Int) -> Unit,
    onNavigateToPlayList: (Uri) -> Unit,
    onShowMusicItemOption: (Uri) -> Unit
) {
    TabRowAndPager(
        modifier = modifier.fillMaxSize(),
        items = HomePage.values().toList(),
        tabRowContent = { page ->
            Text(
                modifier = modifier.padding(vertical = 10.dp),
                text = stringResource(id = page.titleResId)
            )
        },
        pagerContent = { page ->
            when (page) {
                HomePage.AUDIO_PAGE -> {
                    AudioPage(
                        Modifier.fillMaxSize(),
                        onPlayMusicInList = onPlayMusicInList,
                        onShowMusicItemOption = onShowMusicItemOption
                    )
                }
                HomePage.ALBUM_PAGE -> AlbumPage(
                    Modifier.fillMaxSize(),
                    onNavigateToPlayList = onNavigateToPlayList
                )
                HomePage.ARTIST_PAGE -> ArtistPage(
                    Modifier.fillMaxSize(),
                    onNavigateToPlayList = onNavigateToPlayList
                )
            }
        }
    )
}

enum class HomePage(
    @StringRes val titleResId: Int
) {
    AUDIO_PAGE(R.string.audio_page_title),
    ALBUM_PAGE(R.string.album_page_title),
    ARTIST_PAGE(R.string.artist_page_title),
}
