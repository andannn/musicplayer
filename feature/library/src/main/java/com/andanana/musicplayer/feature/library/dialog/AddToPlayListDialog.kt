package com.andanana.musicplayer.feature.library

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Approval
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
import com.andanana.musicplayer.core.designsystem.component.SmpTextButton
import com.andanana.musicplayer.core.designsystem.theme.MusicPlayerTheme

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
        onNewPlayListButtonClick = onNewPlayListButtonClick,
        onApplyButtonClick = {
            playListDialogViewModel.onApplyButtonClick()
            onNavigateBack()
        }
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlayListDialogContent(
    modifier: Modifier = Modifier,
    uiState: PlayListDialogUiState,
    onItemCheckChange: (PlayListItem, Boolean) -> Unit,
    onNewPlayListButtonClick: () -> Unit,
    onApplyButtonClick: () -> Unit
) {
    Card(
        modifier = modifier.wrapContentSize(),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(
            modifier = modifier
                .heightIn(max = 500.dp)
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
                modifier = Modifier.align(CenterHorizontally).weight(1f),
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
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 10.dp),
                        items = state.playListItems,
                        checkedItems = state.checkItemList,
                        onItemCheckChange = onItemCheckChange
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Divider()
            Spacer(modifier = Modifier.height(10.dp))
            SmpTextButton(
                modifier = Modifier.wrapContentHeight().align(CenterHorizontally),
                text = "Apply",
                imageVector = Icons.Rounded.Approval,
                onClick = onApplyButtonClick
            )
        }
    }
}

@Composable
private fun PlayListCheckList(
    modifier: Modifier = Modifier,
    items: List<PlayListItem>,
    checkedItems: List<PlayListItem>,
    onItemCheckChange: (PlayListItem, Boolean) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        items.forEachIndexed { index, playListItem ->
            PlayListCheckItem(
                isChecked = checkedItems.contains(playListItem),
                item = playListItem,
                onCheckedChange = {
                    onItemCheckChange(playListItem, it)
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
            onNewPlayListButtonClick = {},
            onApplyButtonClick = {}
        )
    }
}
