package com.andanana.musicplayer.feature.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.andanana.musicplayer.feature.player.widget.ShrinkablePlayBox

@Composable
fun PlayerSheet(playerStateViewModel: PlayerStateViewModel = hiltViewModel()) {
    val state by playerStateViewModel.playerUiStateFlow.collectAsState()

    if (state is PlayerUiState.Active) {
        ShrinkablePlayBox(
            state = state as PlayerUiState.Active,
            onEvent = playerStateViewModel::onEvent,
        )
    }
}
