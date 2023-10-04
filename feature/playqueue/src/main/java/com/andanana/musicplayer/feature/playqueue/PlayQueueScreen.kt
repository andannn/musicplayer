package com.andanana.musicplayer.feature.playqueue

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andanana.musicplayer.core.designsystem.component.MusicCard
import com.andanana.musicplayer.core.data.model.MusicModel

@Composable
internal fun PlayQueueScreen(
    playQueueViewModel: PlayQueueViewModel = hiltViewModel()
) {
//    val queueList by playQueueViewModel.playQueueFlow.collectAsState(initial = emptyList())
    val playingMedia by playQueueViewModel.playingMediaFlow.collectAsState(null)

    PlayQueueScreenContent(
        queueList = emptyList(),
        playingUri = playingMedia?.localConfiguration?.uri
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlayQueueScreenContent(
    queueList: List<MusicModel>,
    playingUri: Uri?
) {
    LazyColumn(
        modifier = Modifier,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Queue", style = MaterialTheme.typography.titleMedium)
            }
        }
        items(
            items = queueList,
            key = { it.contentUri }
        ) { info ->
            MusicCard(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .animateItemPlacement(),
                albumArtUri = info.albumUri.toString(),
                isActive = playingUri == info.contentUri,
                title = info.title,
                showTrackNum = false,
                showSwapIcon = true,
                artist = info.artist,
                trackNum = info.cdTrackNumber,
                date = info.modifiedDate,
                onMusicItemClick = {
                },
                onOptionButtonClick = {
                }
            )
        }
    }
}
