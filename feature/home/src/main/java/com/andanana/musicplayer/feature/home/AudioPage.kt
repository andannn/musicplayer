package com.andanana.musicplayer.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andanana.musicplayer.core.designsystem.component.MusicCard

private const val TAG = "AudioPage"

@Composable
fun AudioPage(
    modifier: Modifier = Modifier,
    audioPageViewModel: AudioPageViewModel = hiltViewModel()
) {
    val state by audioPageViewModel.audioPageUiState.collectAsState()

    AudioPageContent(
        modifier = modifier,
        state = state
    )
}

@Composable
private fun AudioPageContent(
    modifier: Modifier = Modifier,
    state: AudioPageUiState
) {
    when (state) {
        AudioPageUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        is AudioPageUiState.Ready -> {
            val musicInfoList = state.infoList

            LazyColumn(
                modifier = modifier
            ) {
                items(
                    items = musicInfoList,
                    key = { it.contentUri }
                ) { info ->
                    MusicCard(
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 4.dp),
                        albumArtUri = info.albumUri,
                        title = info.title,
                        artist = info.artist,
                        date = info.modifiedDate
                    )
                }
            }
        }
    }
}
