package com.andanana.musicplayer.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

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
        }
    }
}
