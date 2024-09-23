package com.andannn.melodify.feature.home

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andannn.melodify.core.domain.model.AlbumItemModel
import com.andannn.melodify.core.domain.model.MediaItemModel
import com.andannn.melodify.core.domain.model.ArtistItemModel
import com.andannn.melodify.core.domain.model.AudioItemModel
import com.andannn.melodify.core.designsystem.component.ExtraPaddingBottom
import com.andannn.melodify.core.designsystem.component.LargePreviewCard
import com.andannn.melodify.core.designsystem.component.AudioItemView
import com.andannn.melodify.core.designsystem.theme.MusicPlayerTheme
import com.andannn.melodify.core.domain.model.MediaListSource
import com.andannn.melodify.feature.home.util.ResourceUtil
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    onNavigateToPlayList: (id: String, source: MediaListSource) -> Unit,
) {
    fun onMediaItemClick(mediaItem: MediaItemModel) {
        when (mediaItem) {
            is AlbumItemModel -> {
                onNavigateToPlayList(mediaItem.id.toString(), MediaListSource.ALBUM)
            }

            is ArtistItemModel -> {
                onNavigateToPlayList(mediaItem.id.toString(), MediaListSource.ARTIST)
            }

            is AudioItemModel -> {
                homeViewModel.playMusic(mediaItem)
            }
        }
    }

    val state by homeViewModel.state.collectAsState()

    HomeScreen(
        state = state,
        modifier = modifier,
        onMediaItemClick = ::onMediaItemClick,
        onSelectCategory = homeViewModel::onSelectedCategoryChanged,
        onShowMusicItemOption = homeViewModel::onShowMusicItemOption,
    )
}

@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    state: HomeUiState,
    modifier: Modifier = Modifier,
    onMediaItemClick: (MediaItemModel) -> Unit = {},
    onSelectCategory: (MediaCategory) -> Unit = {},
    onShowMusicItemOption: (AudioItemModel) -> Unit,
) {
    val categories = MediaCategory.entries.toTypedArray()

    val selectedIndex = categories.indexOf(state.currentCategory)

    val mediaItems = state.mediaItems

    val scrollBehavior = enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors =
                TopAppBarDefaults.centerAlignedTopAppBarColors().run {
                    copy(scrolledContainerColor = containerColor)
                },
                title = {
                    Text(text = "Melodify")
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
            ScrollableTabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = selectedIndex,
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

            when (state.currentCategory) {
                MediaCategory.ALL_MUSIC ->
                    LazyAllAudioContent(
                        modifier =
                        Modifier.fillMaxSize(),
                        mediaItems = mediaItems as ImmutableList<AudioItemModel>,
                        onMusicItemClick = onMediaItemClick,
                        onShowMusicItemOption = onShowMusicItemOption
                    )

                MediaCategory.ALBUM -> {
                    LazyAllAlbumContent(
                        modifier =
                        Modifier.fillMaxSize(),
                        mediaItems = mediaItems as ImmutableList<AlbumItemModel>,
                        onItemClick = onMediaItemClick,
                    )
                }

                MediaCategory.ARTIST -> {
                    LazyAllArtistContent(
                        modifier =
                        Modifier.fillMaxSize(),
                        mediaItems = mediaItems as ImmutableList<ArtistItemModel>,
                        onItemClick = onMediaItemClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun LazyAllAlbumContent(
    mediaItems: ImmutableList<AlbumItemModel>,
    modifier: Modifier = Modifier,
    onItemClick: (AlbumItemModel) -> Unit = {},
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Adaptive(180.dp),
    ) {
        items(
            items = mediaItems,
            key = { it.id },
        ) { media ->
            LargePreviewCard(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.dp),
                artCoverUri = Uri.parse(media.artWorkUri),
                title = media.name,
                trackCount = media.trackCount,
                onClick = {
                    onItemClick.invoke(media)
                },
            )
        }

        item { ExtraPaddingBottom() }
    }

}

@Composable
fun LazyAllArtistContent(
    mediaItems: ImmutableList<ArtistItemModel>,
    modifier: Modifier = Modifier,
    onItemClick: (ArtistItemModel) -> Unit = {},
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier.fillMaxSize(),
        columns = StaggeredGridCells.Fixed(2),
    ) {
        items(
            items = mediaItems,
            key = { it.id },
        ) { media ->
            LargePreviewCard(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.dp),
                imageModifier =
                Modifier
                    .clip(shape = CircleShape)
                    .alpha(0.4f)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)),
                placeholder = rememberVectorPainter(Icons.Rounded.Person),
                artCoverUri = Uri.parse(media.artistCoverUri),
                title = media.name,
                trackCount = media.trackCount,
                onClick = {
                    onItemClick.invoke(media)
                },
            )
        }

        item { ExtraPaddingBottom() }
    }
}

@Composable
fun LazyAllAudioContent(
    mediaItems: ImmutableList<AudioItemModel>,
    modifier: Modifier = Modifier,
    onMusicItemClick: (AudioItemModel) -> Unit = {},
    onShowMusicItemOption: (AudioItemModel) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 5.dp),
    ) {
        items(
            items = mediaItems,
            key = { it.id },
        ) { item ->
            AudioItemView(
                modifier =
                Modifier
                    .padding(vertical = 4.dp),
                isActive = false,
                albumArtUri = item.artWorkUri,
                title = item.name,
                showTrackNum = false,
                artist = item.artist,
                trackNum = item.cdTrackNumber,
                onMusicItemClick = {
                    onMusicItemClick.invoke(item)
                },
                onOptionButtonClick = {
                    onShowMusicItemOption(item)
                },
            )
        }

        item { ExtraPaddingBottom() }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    MusicPlayerTheme {
        LazyAllAlbumContent(
            mediaItems = (1..4).map {
                AlbumItemModel(
                    id = it.toLong(),
                    name = "Album $it",
                    artWorkUri = "",
                    trackCount = 10
                )
            }.toImmutableList()
        )
    }
}
