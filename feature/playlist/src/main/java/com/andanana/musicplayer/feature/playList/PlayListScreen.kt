package com.andanana.musicplayer.feature.playList

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import com.andanana.musicplayer.core.data.model.LibraryRootCategory
import com.andanana.musicplayer.core.data.util.buildMediaItem
import com.andanana.musicplayer.core.data.util.isSameDatasource
import com.andanana.musicplayer.core.designsystem.component.MusicCard
import com.andanana.musicplayer.core.designsystem.component.PlayBoxMaxHeight
import com.andanana.musicplayer.core.designsystem.component.PlayBoxMinHeight
import com.andanana.musicplayer.core.designsystem.component.PlayListControlBox
import com.andanana.musicplayer.core.designsystem.theme.MusicPlayerTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private const val TAG = "PlayListScreen"

@Composable
fun PlayListScreen(
    modifier: Modifier = Modifier,
    playListViewModel: PlayListViewModel = hiltViewModel(),
    onShowMusicItemOption: (Uri) -> Unit,
    onShowPlayListItemOption: (Uri) -> Unit,
) {
    val uiState by playListViewModel.state.collectAsState()

    PlayListScreenContent(
        modifier = modifier,
        uiState = uiState,
        onPlayAllButtonClick = {
        },
        onAudioItemClick = playListViewModel::setPlayListAndStartIndex,
        onShowMusicItemOption = onShowMusicItemOption,
        onShowPlayListItemOption = onShowPlayListItemOption,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlayListScreenContent(
    modifier: Modifier = Modifier,
    uiState: PlayListUiState,
    onPlayAllButtonClick: () -> Unit = {},
    onAddButtonClick: () -> Unit = {},
    onAudioItemClick: (List<MediaItem>, Int) -> Unit = { list, index -> },
    onShowMusicItemOption: (Uri) -> Unit = {},
    onShowPlayListItemOption: (Uri) -> Unit = {},
) {
    val playListControlBoxMaxHeightPx =
        with(LocalDensity.current) {
            PlayBoxMaxHeight.toPx()
        }
    val playListControlBoxMinHeightPx =
        with(LocalDensity.current) {
            PlayBoxMinHeight.toPx()
        }
    var playListControlBoxHeight by remember {
        mutableFloatStateOf(playListControlBoxMaxHeightPx)
    }

//    val nestedScrollConnection =
//        remember {
//            object : NestedScrollConnection {
//                override fun onPreScroll(
//                    available: Offset,
//                    source: NestedScrollSource,
//                ): Offset {
//                    val newOffset = playListControlBoxHeight + available.y.div(6f)
//                    playListControlBoxHeight =
//                        newOffset.coerceIn(
//                            playListControlBoxMinHeightPx,
//                            playListControlBoxMaxHeightPx,
//                        )
//                    return super.onPreScroll(available, source)
//                }
//            }
//        }
    val systemUiController = rememberSystemUiController()
    val disposedColor = MaterialTheme.colorScheme.surface
    DisposableEffect(key1 = Unit) {
        systemUiController.setStatusBarColor(Color.Transparent, true)
        onDispose {
            systemUiController.setSystemBarsColor(color = disposedColor)
        }
    }

    var appBarHeight by
        remember {
            mutableIntStateOf(0)
        }

    val appBarHeightDp =
        with(LocalDensity.current) {
            appBarHeight.toDp()
        }

    Box(
        modifier = modifier,
    ) {
        LazyColumn(modifier = Modifier.navigationBarsPadding()) {
            item {
                Spacer(modifier = Modifier.height(appBarHeightDp))
            }
            item {
                Surface {
                    PlayListControlBox(
                        modifier = Modifier.padding(10.dp),
                        height = with(LocalDensity.current) { playListControlBoxHeight.toDp() },
                        coverArtUri = "",
                        title = uiState.title,
                        trackCount = uiState.trackCount,
                        onPlayAllButtonClick = onPlayAllButtonClick,
                        onAddToPlayListButtonClick = {},
                        onOptionButtonClick = { },
                    )
                }
            }

            items(
                items = uiState.musicItems,
                key = { it.mediaId },
            ) { item ->
                MusicCard(
                    modifier =
                        Modifier
                            .padding(vertical = 4.dp)
                            .animateItemPlacement(),
                    isActive = uiState.playingMediaItem?.isSameDatasource(item) == true,
                    albumArtUri = item.mediaMetadata.artworkUri.toString(),
                    title = item.mediaMetadata.title.toString(),
                    showTrackNum = uiState.playListType == LibraryRootCategory.ALBUM,
                    artist = item.mediaMetadata.artist.toString(),
                    trackNum = item.mediaMetadata.trackNumber ?: 0,
                    date = -1,
                    onMusicItemClick = {
                        onAudioItemClick(
                            uiState.musicItems,
                            uiState.musicItems.indexOf(item),
                        )
                    },
                    onOptionButtonClick = {
                        item.localConfiguration?.let { onShowMusicItemOption(it.uri) }
                    },
                )
            }
        }

        CustomAppTopBar(
            modifier =
                Modifier.onSizeChanged {
                    appBarHeight = it.height
                },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomAppTopBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {
    TopAppBar(
        modifier =
            modifier
                .background(MaterialTheme.colorScheme.primary),
        title = {
            Text(text = "ASS")
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Image(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                )
            }
        },
    )
}

@Preview
@Composable
private fun PlayListScreenContentPreview() {
    MusicPlayerTheme {
        PlayListScreenContent(
            uiState =
                PlayListUiState(
                    title = "Title",
                    trackCount = 12,
                    musicItems =
                        listOf(
                            buildMediaItem(
                                title = "test song A",
                                mediaId = "AAA",
                                isPlayable = true,
                                isBrowsable = false,
                                mediaType = 0,
                            ),
                        ),
                ),
        )
    }
}
