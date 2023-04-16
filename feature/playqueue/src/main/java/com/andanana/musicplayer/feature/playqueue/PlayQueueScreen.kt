package com.andanana.musicplayer.feature.playqueue

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.andanana.musicplayer.core.model.MusicInfo

@Composable
internal fun PlayQueueScreen(
    playQueueViewModel: PlayQueueViewModel = hiltViewModel()
) {
    val queueList by playQueueViewModel.playQueueFlow.collectAsState(initial = emptyList())

    PlayQueueScreenContent(
        queueList = queueList
    )
}

@Composable
private fun PlayQueueScreenContent(
    queueList: List<MusicInfo>
) {

}
