package com.andanana.musicplayer.feature.home

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import com.andanana.musicplayer.core.designsystem.component.CenterTabLayout
import com.andanana.musicplayer.core.designsystem.component.ExtraPaddingBottom
import com.andanana.musicplayer.core.designsystem.component.LargePreviewCard
import com.andanana.musicplayer.core.designsystem.component.MusicCard
import com.andanana.musicplayer.core.model.ALBUM_ID
import com.andanana.musicplayer.core.model.ALL_MUSIC_ID
import com.andanana.musicplayer.core.model.ARTIST_ID
import com.andanana.musicplayer.feature.home.util.ResourceUtil

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    onNavigateToPlayList: (mediaId: String) -> Unit,
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
            onSelectCategory = homeViewModel::onSelectedCategoryChanged,
            onMediaItemClick = ::onMediaItemClick,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    state: HomeUiState,
    modifier: Modifier = Modifier,
    onMediaItemClick: (MediaItem) -> Unit = {},
    onSelectCategory: (String) -> Unit = {},
) {
    val categories =
        state.categories.map {
            it.mediaId
        }

    val selectedIndex = categories.indexOf(state.mediaItemPair.first)

    val mediaItems =
        remember(state) {
            state.categoryPageContents
        }

    val scrollBehavior = enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                colors =
                    TopAppBarDefaults.centerAlignedTopAppBarColors().run {
                        copy(scrolledContainerColor = containerColor)
                    },
                title = {
                    Text(text = "Simple music player")
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .fillMaxSize(),
        ) {
            CenterTabLayout(
                modifier = Modifier.fillMaxWidth(),
                paddingVertical = 5.dp,
                selectedIndex = selectedIndex,
                onScrollFinishToSelectIndex = { index ->
                    onSelectCategory.invoke(categories[index])
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
                            onSelectCategory.invoke(categories[index])
                        },
                    )
                }
            }

            when (state.mediaItemPair.first) {
                ALL_MUSIC_ID -> {
                    LazyAllAudioContent(
                        modifier =
                            Modifier.fillMaxSize(),
                        mediaItems = mediaItems,
                        onMusicItemClick = onMediaItemClick,
                    )
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

                        item { ExtraPaddingBottom() }
                    }
                }

                ARTIST_ID -> {
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
                                imageModifier =
                                    Modifier
                                        .clip(shape = CircleShape)
                                        .alpha(0.4f)
                                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)),
                                placeholder = rememberVectorPainter(Icons.Rounded.Person),
                                artCoverUri = media.mediaMetadata.artworkUri ?: Uri.EMPTY,
                                title = media.mediaMetadata.title.toString(),
                                trackCount = media.mediaMetadata.totalTrackCount ?: 0,
                                onClick = {
                                    onMediaItemClick.invoke(media)
                                },
                            )
                        }

                        item { ExtraPaddingBottom() }
                    }
                }
            }
        }
    }
}

// TODO: remove dependency of MediaItem
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyAllAudioContent(
    mediaItems: List<MediaItem>,
    modifier: Modifier = Modifier,
    onMusicItemClick: (MediaItem) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
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
                onMusicItemClick = {
                    onMusicItemClick.invoke(item)
                },
                onOptionButtonClick = {
//                                item.localConfiguration?.let { onShowMusicItemOption(it.uri) }
                },
            )
        }

        item { ExtraPaddingBottom() }
    }
}
