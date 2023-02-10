package com.andanana.musicplayer.feature.playList

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PlayListScreen(
    playListViewModel: PlayListViewModel = hiltViewModel()
) {
    playListViewModel
}
