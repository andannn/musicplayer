package com.andanana.musicplayer.feature.library

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andanana.musicplayer.core.database.usecases.FAVORITE_PLAY_LIST_ID
import com.andanana.musicplayer.core.designsystem.component.PlayListCard

@Composable
fun PlayListScreen(
    modifier: Modifier = Modifier,
    playListViewModel: PlayListViewModel = hiltViewModel(),
    onPlayListItemClick: (PlayListItem) -> Unit,
    onOptionButtonClick: (PlayListItem) -> Unit
) {
    val state by playListViewModel.playListPageUiState.collectAsState()
    PlayListPage(
        modifier = modifier,
        state = state,
        onPlayListItemClick = onPlayListItemClick,
        onOptionButtonClick = onOptionButtonClick
    )
}

@Composable
private fun PlayListPage(
    modifier: Modifier,
    state: PlayListPageUiState,
    onPlayListItemClick: (PlayListItem) -> Unit,
    onOptionButtonClick: (PlayListItem) -> Unit
) {
    when (state) {
        PlayListPageUiState.Loading -> {}
        is PlayListPageUiState.Ready -> {
            PlayListPageContent(
                modifier = modifier,
                itemList = state.infoMap.map { it.value },
                onPlayListItemClick = onPlayListItemClick,
                onOptionButtonClick = onOptionButtonClick
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlayListPageContent(
    modifier: Modifier = Modifier,
    itemList: List<PlayListItem>,
    onPlayListItemClick: (PlayListItem) -> Unit,
    onOptionButtonClick: (PlayListItem) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
    ) {
        items(
            items = itemList,
            key = { it.id }
        ) { item ->
            val coverImage = if (item.id == FAVORITE_PLAY_LIST_ID) {
                Icons.Rounded.FavoriteBorder
            } else {
                null
            }
            PlayListCard(
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .animateItemPlacement(),
                title = item.name,
                coverImage = coverImage,
                trackCount = item.count,
                onPlayListItemClick = {
                    onPlayListItemClick(item)
                },
                onOptionButtonClick = {
                    onOptionButtonClick(item)
                }
            )
        }
    }
}
