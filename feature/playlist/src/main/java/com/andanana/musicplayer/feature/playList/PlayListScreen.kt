package com.andanana.musicplayer.feature.playList

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andanana.musicplayer.core.designsystem.component.MusicCard
import com.andanana.musicplayer.core.designsystem.component.PlayBoxMaxHeight
import com.andanana.musicplayer.core.designsystem.component.PlayBoxMinHeight
import com.andanana.musicplayer.core.designsystem.component.PlayListControlBox
import com.andanana.musicplayer.core.model.MusicInfo

private const val TAG = "PlayListScreen"

@Composable
fun PlayListScreen(
    playListViewModel: PlayListViewModel = hiltViewModel()
) {
    val coverArtUri by playListViewModel.artCoverUri.collectAsState(initial = Uri.EMPTY)
    val musicItems by playListViewModel.musicItemsFlow.collectAsState(emptyList())
    val trackCount = remember(musicItems) { musicItems.count() }
    PlayListContent(
        coverArtUri = coverArtUri.toString(),
        title = "Title",
        trackCount = trackCount,
        musicItems = musicItems
    )
}

@Composable
fun PlayListContent(
    modifier: Modifier = Modifier,
    coverArtUri: String,
    title: String,
    musicItems: List<MusicInfo>,
    trackCount: Int,
    onPlayAllButtonClick: () -> Unit = {},
    onAddToPlayListButtonClick: () -> Unit = {}
) {
    val playListControlBoxMaxHeightPx = with(LocalDensity.current) {
        PlayBoxMaxHeight.toPx()
    }
    val playListControlBoxMinHeightPx = with(LocalDensity.current) {
        PlayBoxMinHeight.toPx()
    }
    var playListControlBoxHeight by remember {
        mutableStateOf(playListControlBoxMaxHeightPx)
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val newOffset = playListControlBoxHeight + available.y.div(3f)
                playListControlBoxHeight = newOffset.coerceIn(playListControlBoxMinHeightPx, playListControlBoxMaxHeightPx)
                return super.onPreScroll(available, source)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .nestedScroll(nestedScrollConnection)
    ) {
        Column {
            PlayListControlBox(
                modifier = Modifier.padding(20.dp),
                height = with(LocalDensity.current) {
                    playListControlBoxHeight.toDp()
                },
                coverArtUri = coverArtUri,
                title = title,
                trackCount = trackCount,
                onPlayAllButtonClick = onPlayAllButtonClick,
                onAddToPlayListButtonClick = onAddToPlayListButtonClick
            )

            LazyColumn(
                modifier = modifier.nestedScroll(nestedScrollConnection),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
            ) {
                items(
                    items = musicItems,
                    key = { it.contentUri }
                ) { info ->
                    MusicCard(
                        modifier = Modifier.padding(vertical = 4.dp),
                        albumArtUri = info.albumUri,
                        title = info.title,
                        artist = info.artist,
                        date = info.modifiedDate,
                        onMusicItemClick = {
                        }
                    )
                }
            }
        }
    }
}
