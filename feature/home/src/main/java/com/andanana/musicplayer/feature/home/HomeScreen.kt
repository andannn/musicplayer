package com.andanana.musicplayer.feature.home

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import com.andanana.musicplayer.core.designsystem.component.CenterTabLayout
import com.andanana.musicplayer.core.designsystem.component.LargePreviewCard
import com.andanana.musicplayer.feature.home.util.ResourceUtil

private const val TAG = "HomeScreen"

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    onNavigateToPlayList: (mediaId: String) -> Unit,
    onShowMusicItemOption: (Uri) -> Unit
) {
    fun onMediaItemClick(mediaItem: MediaItem) {
        if (mediaItem.mediaMetadata.isBrowsable == true) {
            onNavigateToPlayList(mediaItem.mediaId)
        }
    }

    val state by homeViewModel.state.collectAsState()

    if (state is HomeUiState.Loading) {
        Box(modifier = modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
    } else {
        HomeScreen(
            modifier = modifier,
            state = state as HomeUiState.Ready,
            onTabClicked = homeViewModel::onSelectedCategoryChanged,
            onMediaItemClick = ::onMediaItemClick
        )
    }
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeUiState.Ready,
    onMediaItemClick: (MediaItem) -> Unit,
    onTabClicked: (String) -> Unit
) {
    val categories = state.categories.map {
        it.mediaId
    }
    val selectedIndex = categories.indexOf(state.selectedCategory)

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        CenterTabLayout(
            modifier = Modifier.fillMaxWidth(),
            paddingVertical = 5.dp,
            selectedIndex = selectedIndex,
            onScrollFinishToSelectIndex = { index ->
                if (index != selectedIndex) {
                    onTabClicked.invoke(categories[index])
                }
            }
        ) {
            categories.forEachIndexed { index, item ->
                Tab(
                    modifier = Modifier,
                    selected = index == selectedIndex,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                    text = @Composable {
                        Text(
                            text = stringResource(id = ResourceUtil.getCategoryResource(item))
                        )
                    },
                    onClick = {
                        if (index != selectedIndex) {
                            onTabClicked.invoke(categories[index])
                        }
                    }
                )
            }
        }

        LazyVerticalStaggeredGrid(
            modifier = modifier,
            columns = StaggeredGridCells.Fixed(2)
        ) {
            items(
                items = state.currentMusicItems,
                key = { it.mediaId }
            ) { media ->
                LargePreviewCard(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.dp),
                    artCoverUri = media.mediaMetadata.artworkUri ?: Uri.EMPTY,
                    title = media.mediaMetadata.title.toString(),
                    trackCount = media.mediaMetadata.totalTrackCount ?: 0,
                    onClick = {
                        onMediaItemClick.invoke(media)
                    }
                )
            }
        }
    }
}
