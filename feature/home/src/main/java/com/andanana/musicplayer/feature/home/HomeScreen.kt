package com.andanana.musicplayer.feature.home

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import com.andanana.musicplayer.core.data.model.ALBUM_ID
import com.andanana.musicplayer.core.data.model.ALL_MUSIC_ID
import com.andanana.musicplayer.core.data.model.ARTIST_ID
import com.andanana.musicplayer.core.designsystem.component.CenterTabLayout
import com.andanana.musicplayer.core.designsystem.component.LargePreviewCard
import com.andanana.musicplayer.core.designsystem.component.MusicCard
import com.andanana.musicplayer.feature.home.util.ResourceUtil
import kotlinx.coroutines.launch

private const val TAG = "HomeScreen"

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    onNavigateToPlayList: (mediaId: String) -> Unit,
    onShowMusicItemOption: (Uri) -> Unit,
) {
    fun onMediaItemClick(mediaItem: MediaItem) {
        if (mediaItem.mediaMetadata.isBrowsable == true) {
            onNavigateToPlayList(mediaItem.mediaId)
        } else {
            homeViewModel.playMusic(mediaItem)
        }
    }

    val state by homeViewModel.state.collectAsState()

    if (state.categories.isNotEmpty()) {
        HomeScreen(
            modifier = modifier,
            state = state,
            onTabClicked = homeViewModel::onSelectedCategoryChanged,
            onMediaItemClick = ::onMediaItemClick,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeUiState,
    onMediaItemClick: (MediaItem) -> Unit,
    onTabClicked: (String) -> Unit,
) {
    val categories =
        state.categories.map {
            it.mediaId
        }

    val categoryPageContents =
        remember(state) {
            state.categoryPageContents
        }

    val pageState = rememberPagerState(pageCount = { categories.size })

    val selectedIndex = pageState.currentPage

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        Log.d(TAG, "HomeScreen: LaunchedEffect ${pageState.currentPage}")
        snapshotFlow {
            pageState.currentPage
        }.collect {
            Log.d(TAG, "HomeScreen: request ${categories[it]}")
            onTabClicked.invoke(categories[it])
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Smp")
                },
            )
        },
    ) {
        Column(
            modifier =
                Modifier
                    .padding(it)
                    .fillMaxSize(),
        ) {
            CenterTabLayout(
                modifier = Modifier.fillMaxWidth(),
                paddingVertical = 5.dp,
                selectedIndex = selectedIndex,
                onScrollFinishToSelectIndex = { index ->
                    scope.launch {
                        pageState.animateScrollToPage(index)
                    }
                },
            ) {
                categories.forEachIndexed { index, item ->
                    Tab(
                        modifier = Modifier,
                        selected = index == selectedIndex,
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                        text = @Composable {
                            Text(
                                text = stringResource(id = ResourceUtil.getCategoryResource(item)),
                            )
                        },
                        onClick = {
                            scope.launch {
                                pageState.animateScrollToPage(index)
                            }
                        },
                    )
                }
            }

            HorizontalPager(state = pageState) { index ->
                val mediaItems = categoryPageContents[index]
                when (categories[index]) {
                    ALL_MUSIC_ID -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 5.dp),
                        ) {
                            items(
                                items = mediaItems,
                                key = { it.mediaId },
                            ) { item ->
                                MusicCard(
                                    modifier =
                                        Modifier
                                            .padding(vertical = 4.dp)
                                            .animateItemPlacement(),
                                    isActive = false,
                                    albumArtUri = item.mediaMetadata.artworkUri.toString(),
                                    title = item.mediaMetadata.title.toString(),
                                    showTrackNum = false,
                                    artist = item.mediaMetadata.artist.toString(),
                                    trackNum = item.mediaMetadata.trackNumber ?: 0,
                                    date = -1,
                                    onMusicItemClick = {
                                        onMediaItemClick.invoke(item)
                                    },
                                    onOptionButtonClick = {
//                                item.localConfiguration?.let { onShowMusicItemOption(it.uri) }
                                    },
                                )
                            }
                        }
                    }

                    ALBUM_ID -> {
                        LazyVerticalStaggeredGrid(
                            modifier = Modifier.fillMaxSize(),
                            columns = StaggeredGridCells.Fixed(2),
                        ) {
                            items(
                                items = mediaItems,
                                key = { it.mediaId },
                            ) { media ->
                                LargePreviewCard(
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.dp),
                                    artCoverUri = media.mediaMetadata.artworkUri ?: Uri.EMPTY,
                                    title = media.mediaMetadata.title.toString(),
                                    trackCount = media.mediaMetadata.totalTrackCount ?: 0,
                                    onClick = {
                                        onMediaItemClick.invoke(media)
                                    },
                                )
                            }
                        }
                    }

                    ARTIST_ID -> {
                    }
                }
            }
        }
    }
}
