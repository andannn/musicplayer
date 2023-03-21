package com.andanana.musicplayer.feature.playList

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andanana.musicplayer.core.designsystem.component.AnimatedUpdateList
import com.andanana.musicplayer.core.designsystem.component.MusicCard
import com.andanana.musicplayer.core.designsystem.component.PlayBoxMaxHeight
import com.andanana.musicplayer.core.designsystem.component.PlayBoxMinHeight
import com.andanana.musicplayer.core.designsystem.component.PlayListControlBox
import com.andanana.musicplayer.core.model.MusicInfo
import com.andanana.musicplayer.core.model.RequestType

private const val TAG = "PlayListScreen"

@Composable
fun PlayListScreen(
    playListViewModel: PlayListViewModel = hiltViewModel(),
    onPlayMusicInList: (List<MusicInfo>, Int) -> Unit,
    onShowMusicItemOption: (Uri) -> Unit,
    onShowPlayListItemOption: (Uri) -> Unit
) {
    val uiState by playListViewModel.playListUiStateFlow.collectAsState()

    PlayListScreenContent(
        uiState = uiState,
        onPlayAllButtonClick = {
            (uiState as? PlayListUiState.Ready)?.let {
                onPlayMusicInList(it.musicItems, 0)
            }
        },
        onAudioItemClick = onPlayMusicInList,
        onShowMusicItemOption = onShowMusicItemOption,
        onShowPlayListItemOption = onShowPlayListItemOption
    )
}

@Composable
private fun PlayListScreenContent(
    uiState: PlayListUiState,
    onPlayAllButtonClick: () -> Unit = {},
    onAddButtonClick: () -> Unit = {},
    onAudioItemClick: (List<MusicInfo>, Int) -> Unit,
    onShowMusicItemOption: (Uri) -> Unit,
    onShowPlayListItemOption: (Uri) -> Unit
) {
    when (uiState) {
        PlayListUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        is PlayListUiState.Ready -> {
            PlayListContent(
                coverArtUri = uiState.artCoverUri,
                type = uiState.type,
                title = uiState.title,
                activeMusic = uiState.interactingUri,
                musicItems = uiState.musicItems,
                trackCount = uiState.trackCount,
                onPlayAllButtonClick = onPlayAllButtonClick,
                onAudioItemClick = onAudioItemClick,
                onShowMusicItemOption = onShowMusicItemOption,
                onOptionButtonClick = { onShowPlayListItemOption(uiState.contentUri) }
            )
        }
    }
}

@Composable
private fun PlayListContent(
    modifier: Modifier = Modifier,
    coverArtUri: String,
    type: RequestType,
    title: String,
    activeMusic: Uri?,
    musicItems: List<MusicInfo>,
    trackCount: Int,
    onPlayAllButtonClick: () -> Unit = {},
    onAddToPlayListButtonClick: () -> Unit = {},
    onAudioItemClick: (List<MusicInfo>, Int) -> Unit,
    onShowMusicItemOption: (Uri) -> Unit,
    onOptionButtonClick: () -> Unit
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
                val newOffset = playListControlBoxHeight + available.y.div(6f)
                playListControlBoxHeight =
                    newOffset.coerceIn(playListControlBoxMinHeightPx, playListControlBoxMaxHeightPx)
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
            Surface {
                val imageUri = remember(coverArtUri, musicItems) {
                    coverArtUri.ifBlank {
                        musicItems.firstOrNull()?.albumUri ?: ""
                    }
                }

                PlayListControlBox(
                    modifier = Modifier.padding(20.dp),
                    height = with(LocalDensity.current) { playListControlBoxHeight.toDp() },
                    coverArtUri = imageUri,
                    title = title,
                    trackCount = trackCount,
                    onPlayAllButtonClick = onPlayAllButtonClick,
                    onAddToPlayListButtonClick = onAddToPlayListButtonClick,
                    onOptionButtonClick = onOptionButtonClick
                )
            }

            AnimatedUpdateList(
                list = musicItems,
                content = { info ->
                    MusicCard(
                        modifier = Modifier.padding(vertical = 4.dp),
                        colors = if (activeMusic == info.contentUri) {
                            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inversePrimary)
                        } else {
                            CardDefaults.cardColors()
                        },
                        albumArtUri = info.albumUri,
                        title = info.title,
                        showTrackNum = type == RequestType.ALBUM_REQUEST,
                        artist = info.artist,
                        trackNum = info.cdTrackNumber,
                        date = info.modifiedDate,
                        onMusicItemClick = {
                            onAudioItemClick(musicItems, musicItems.indexOf(info))
                        },
                        onOptionButtonClick = {
                            onShowMusicItemOption(info.contentUri)
                        }
                    )
                }
            )
        }
    }
}
