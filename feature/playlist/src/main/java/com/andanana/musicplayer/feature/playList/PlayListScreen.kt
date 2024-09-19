package com.andanana.musicplayer.feature.playList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andanana.musicplayer.core.data.model.AudioItem
import com.andanana.musicplayer.core.designsystem.component.ExtraPaddingBottom
import com.andanana.musicplayer.core.designsystem.component.MusicCard
import com.andanana.musicplayer.core.designsystem.component.PlayListHeader
import com.andanana.musicplayer.core.model.LibraryRootCategory
import com.andannn.musicplayer.common.drawer.MediaBottomSheet

@Composable
fun PlayListScreen(
    modifier: Modifier = Modifier,
    viewModel: PlayListViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
) {
    val uiState by viewModel.state.collectAsState()
    val bottomSheetModel by viewModel.bottomSheetModel.collectAsState()

    val isAlbumType = true
//        remember(uiState.playListType) {
//            uiState.playListType == LibraryRootCategory.ALBUM
//        }

    if (isAlbumType) {
        AlbumPlayListContent(
            modifier = modifier,
            uiState = uiState,
            onEvent = viewModel::onEvent,
            onBackPressed = onBackPressed,
        )
    } else {
        CommonPlayListContent(
            modifier = modifier,
            uiState = uiState,
            onEvent = viewModel::onEvent,
            onBackPressed = onBackPressed,
        )
    }

    if (bottomSheetModel != null) {
        MediaBottomSheet(
            bottomSheet = bottomSheetModel!!.bottomSheet,
            onDismissRequest = {
                viewModel.onEvent(PlayListEvent.OnDismissRequest(it))
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonPlayListContent(
    uiState: PlayListUiState,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    onEvent: (PlayListEvent) -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "uiState.title")
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) {
        LazyColumn(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            items(
                items = uiState.audioList,
                key = { item -> item.id },
            ) { item: AudioItem ->
                MusicCard(
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 10.dp),
                    isActive = uiState.playingMediaItem?.id == item.id,
                    albumArtUri = "",
                    title = item.name,
                    showTrackNum = true,
                    artist = item.artist,
                    trackNum = item.cdTrackNumber,
                    onMusicItemClick = {
//                        onEvent(
//                            PlayListEvent.OnStartPlayAtIndex(
//                                mediaItems = uiState.musicItems,
//                                index = uiState.musicItems.indexOf(item),
//                            ),
//                        )
                    },
                    onOptionButtonClick = {
                        onEvent.invoke(PlayListEvent.OnOptionClick(item))
                    },
                )
            }

            item { ExtraPaddingBottom() }
        }
    }
}

@Composable
private fun AlbumPlayListContent(
    uiState: PlayListUiState,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    onEvent: (PlayListEvent) -> Unit = {},
) {
    var appBarHeight by remember {
        mutableIntStateOf(0)
    }

    val appBarHeightDp =
        with(LocalDensity.current) {
            appBarHeight.toDp()
        }

    var headerHeight by remember {
        mutableIntStateOf(0)
    }

    /**
     * Animation Start Edge: Scroll 0:
     * Status bar is transparent, App bar is transparent. PlayBox alpha is 1.
     *
     * Animation End Edge: Scroll x is Height of PlayBox:
     * Status bar is surface color, App bar is surface color. PlayBox alpha is 0.
     *
     * PlayBox alpha need to be animated from 0 to 1.
     * App bar title need to be animated visible.
     */

    val lazyListState = rememberLazyListState()

    val headerScrollFactor by remember {
        derivedStateOf {
            if (lazyListState.firstVisibleItemIndex == 0 && appBarHeight != 0 && headerHeight != 0) {
                lazyListState.firstVisibleItemScrollOffset.toFloat().div(headerHeight)
                    .coerceIn(0f, 1f)
            } else if (lazyListState.firstVisibleItemIndex >= 1) {
                1f
            } else {
                0f
            }
        }
    }

    val isHeaderVisible by remember {
        derivedStateOf {
            headerScrollFactor != 1f
        }
    }

    val isAppbarTitleVisible by remember {
        derivedStateOf {
            headerScrollFactor >= 0.7f
        }
    }

    Scaffold(
        modifier = modifier,
    ) { padding ->
        // ignore warning.
        padding

        LazyColumn(
            state = lazyListState,
        ) {
            item {
                Column {
                    Spacer(
                        modifier = Modifier.padding(top = appBarHeightDp),
                    )
                    PlayListHeader(
                        modifier =
                        Modifier
                            .padding(10.dp)
                            .graphicsLayer {
                                alpha = 1 - headerScrollFactor
                            }
                            .onSizeChanged {
                                headerHeight = it.height
                            },
                        coverArtUri = uiState.album.artWorkUri,
                        title = uiState.album.name,
                        trackCount = uiState.album.trackCount,
                        onPlayAllButtonClick = {
                            onEvent(
                                PlayListEvent.OnPlayAllButtonClick
                            )
                        },
                        onShuffleButtonClick = {
                            onEvent(
                                PlayListEvent.OnShuffleButtonClick
                            )
                        },
                        onOptionClick = {
                            onEvent(PlayListEvent.OnHeaderOptionClick)
                        }
                    )
                }
            }

            items(
                items = uiState.audioList,
                key = { it.id },
            ) { item ->
                MusicCard(
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 14.dp),
                    isActive = uiState.playingMediaItem?.id == item.id,
                    albumArtUri = uiState.album.artWorkUri,
                    title = item.name,
                    showTrackNum = true,
                    artist = item.artist,
                    trackNum = item.cdTrackNumber,
                    onMusicItemClick = {
                        onEvent(
                            PlayListEvent.OnStartPlayAtIndex(
                                index = uiState.audioList.indexOf(item),
                            ),
                        )
                    },
                    onOptionButtonClick = {
                        onEvent(
                            PlayListEvent.OnOptionClick(
                                item,
                            ),
                        )
                    },
                )
            }

            item { ExtraPaddingBottom() }
        }

        CustomAppTopBar(
            modifier = Modifier.onSizeChanged {
                appBarHeight = it.height
            },
            isBackgroundTransparent = isHeaderVisible,
            isTitleVisible = isAppbarTitleVisible,
            title = uiState.album.name,
            onBackPressed = onBackPressed,
        )
    }
}

@Composable
private fun CustomAppTopBar(
    title: String,
    isTitleVisible: Boolean,
    isBackgroundTransparent: Boolean,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
) {
    Row(
        modifier =
        modifier
            .background(color = if (isBackgroundTransparent) Color.Transparent else MaterialTheme.colorScheme.surface)
            .windowInsetsPadding(WindowInsets.statusBars)
            .fillMaxWidth()
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onBackPressed,
            colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
            )
        }
        AnimatedVisibility(
            visible = isTitleVisible,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview
@Composable
private fun PlayListScreenContentPreview() {
//    MusicPlayerTheme {
//        AlbumPlayListContent(
//            uiState =
//                PlayListUiState(
//                    title = "Title",
//                    trackCount = 12,
//                    musicItems =
//                        listOf(
//                            com.andanana.musicplayer.core.player.buildMediaItem(
//                                title = "test song A",
//                                mediaId = "AAA",
//                                isPlayable = true,
//                                isBrowsable = false,
//                                mediaType = 0,
//                            ),
//                        ),
//                ),
//            onBackPressed = {},
//        )
//    }
}
