package com.andanana.musicplayer.feature.library.dialog

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andanana.musicplayer.core.designsystem.theme.MusicPlayerTheme
import com.andanana.musicplayer.feature.library.PlayListItem

@Composable
fun PlayListDialog(
    playListDialogViewModel: PlayListDialogViewModel = hiltViewModel(),
    onNewPlayListButtonClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by playListDialogViewModel.uiState.collectAsState()

    PlayListDialogContent(
        uiState = uiState,
        onItemCheckChange = playListDialogViewModel::onItemCheckChange,
        onNewPlayListButtonClick = onNewPlayListButtonClick
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlayListDialogContent(
    modifier: Modifier = Modifier,
    uiState: PlayListDialogUiState,
    onItemCheckChange: (PlayListItem, Boolean) -> Unit,
    onNewPlayListButtonClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Save music to..",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = onNewPlayListButtonClick) {
                    Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                    Text(text = "New playlist")
                }
            }
            Divider()
            Spacer(modifier = Modifier.height(10.dp))

            val isReadyState = uiState is PlayListDialogUiState.Ready
            AnimatedContent(
                modifier = Modifier.align(CenterHorizontally).height(400.dp),
                targetState = isReadyState
            ) { isReady ->
                if (!isReady) {
                    Box(modifier = Modifier.padding(10.dp).fillMaxWidth().height(50.dp)) {
                        Text(
                            modifier = Modifier.align(Center),
                            text = "Loading ...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    val state = uiState as PlayListDialogUiState.Ready
                    PlayListCheckList(
                        modifier = Modifier
                            .fillMaxWidth(),
                        favoriteItem = state.favoriteItem,
                        items = state.playListItemWithoutFavorite,
                        checkedItems = state.checkItemList,
                        onItemCheckChange = onItemCheckChange
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun PlayListCheckList(
    modifier: Modifier = Modifier,
    items: List<PlayListItem>,
    favoriteItem: PlayListItem?,
    checkedItems: List<PlayListItem>,
    onItemCheckChange: (PlayListItem, Boolean) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 10.dp)
    ) {
        item {
            favoriteItem?.let { item ->
                PlayListCheckItem(
                    isChecked = checkedItems.contains(item),
                    item = item,
                    onCheckedChange = {
                        onItemCheckChange(item, it)
                    }
                )
            }
        }
        item {
            Divider(color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        items(
            items = items,
            key = { it.id }
        ) { item ->
            PlayListCheckItem(
                isChecked = checkedItems.contains(item),
                item = item,
                onCheckedChange = {
                    onItemCheckChange(item, it)
                }
            )
        }
    }
}

@Composable
private fun PlayListCheckItem(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    item: PlayListItem,
    onCheckedChange: ((Boolean) -> Unit)?
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = isChecked, onCheckedChange = onCheckedChange)
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = item.name, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview
@Composable
private fun PlayListDialogContentPreview() {
    MusicPlayerTheme {
        PlayListDialogContent(
            uiState = PlayListDialogUiState.Ready(
                playListItems = listOf(
                    PlayListItem(id = 0L, "Test A"),
                    PlayListItem(id = 0L, "Test B"),
                    PlayListItem(id = 0L, "Test C"),
                    PlayListItem(id = 0L, "Test D")
                )
            ),
            onItemCheckChange = { _, _ -> },
            onNewPlayListButtonClick = {}
        )
    }
}
