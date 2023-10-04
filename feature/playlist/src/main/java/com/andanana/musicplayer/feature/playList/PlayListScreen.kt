package com.andanana.musicplayer.feature.playList

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
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
import androidx.media3.common.MediaItem
import com.andanana.musicplayer.core.designsystem.component.MusicCard
import com.andanana.musicplayer.core.designsystem.component.PlayBoxMaxHeight
import com.andanana.musicplayer.core.designsystem.component.PlayBoxMinHeight
import com.andanana.musicplayer.core.designsystem.component.PlayListControlBox
import com.andanana.musicplayer.core.data.model.MusicListType

private const val TAG = "PlayListScreen"

@Composable
fun PlayListScreen(
    playListViewModel: PlayListViewModel = hiltViewModel(),
    onShowMusicItemOption: (Uri) -> Unit,
    onShowPlayListItemOption: (Uri) -> Unit
) {
    val uiState by playListViewModel.playListUiStateFlow.collectAsState()

    PlayListScreenContent(
        uiState = uiState,
        onPlayAllButtonClick = {
//            (uiState as? PlayListUiState.Ready)?.let {
//                playListViewModel.setPlayListAndStartIndex(
//                    it.musicItems.map { it.contentUri },
//                    0
//                )
//            }
        },
        onAudioItemClick = playListViewModel::setPlayListAndStartIndex,
        onShowMusicItemOption = onShowMusicItemOption,
        onShowPlayListItemOption = onShowPlayListItemOption
    )
}

@Composable
private fun PlayListScreenContent(
    uiState: PlayListUiState,
    onPlayAllButtonClick: () -> Unit = {},
    onAddButtonClick: () -> Unit = {},
    onAudioItemClick: (List<MediaItem>, Int) -> Unit,
    onShowMusicItemOption: (Uri) -> Unit,
    onShowPlayListItemOption: (Uri) -> Unit
) {
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlayListContent(
    modifier: Modifier = Modifier,
    coverArtUri: String,
    type: MusicListType,
    title: String,
    activeMusic: Uri?,
    musicItems: List<MediaItem>,
    trackCount: Int,
    onPlayAllButtonClick: () -> Unit = {},
    onAddToPlayListButtonClick: () -> Unit = {},
    onAudioItemClick: (List<MediaItem>, Int) -> Unit,
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
//                val imageUri = remember(coverArtUri, musicItems) {
//                    coverArtUri.ifBlank {
//                        musicItems.firstOrNull()?.albumUri ?: ""
//                    }
//                }

                PlayListControlBox(
                    modifier = Modifier.padding(20.dp),
                    height = with(LocalDensity.current) { playListControlBoxHeight.toDp() },
                    coverArtUri = "",
                    title = title,
                    trackCount = trackCount,
                    onPlayAllButtonClick = onPlayAllButtonClick,
                    onAddToPlayListButtonClick = onAddToPlayListButtonClick,
                    onOptionButtonClick = onOptionButtonClick
                )
            }

            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
            ) {
                items(
                    items = musicItems,
                    key = { it }
                ) { item ->
                    MusicCard(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .animateItemPlacement(),
                        isActive = activeMusic == item.localConfiguration?.uri,
                        albumArtUri = item.mediaMetadata.artworkUri.toString(),
                        title = item.mediaMetadata.title.toString(),
                        showTrackNum = type == MusicListType.ALBUM_REQUEST,
                        artist = item.mediaMetadata.artist.toString(),
                        trackNum = item.mediaMetadata.trackNumber ?: 0,
                        date = -1,
                        onMusicItemClick = {
                            onAudioItemClick(
                                musicItems,
                                musicItems.indexOf(item)
                            )
                        },
                        onOptionButtonClick = {
                            item.localConfiguration?.let { onShowMusicItemOption(it.uri) }
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(300.dp))
                }
            }
        }
    }
}
