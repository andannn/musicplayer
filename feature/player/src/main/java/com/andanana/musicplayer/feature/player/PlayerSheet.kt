package com.andanana.musicplayer.feature.player

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.andanana.musicplayer.feature.player.widget.ShrinkablePlayBox

private const val TAG = "PlayerSheet"

@Composable
fun PlayerSheet(
    playerStateViewModel: PlayerStateViewModel = hiltViewModel(),
    onNavigateToPlayer: () -> Unit,
    onToggleFavorite: (Uri) -> Unit,
) {
    val state by playerStateViewModel.playerUiStateFlow.collectAsState()

    Log.d(TAG, "MusicPlayerBox: trigger by state")
    if (state is PlayerUiState.Active) {
        ShrinkablePlayBox(
            state = state as PlayerUiState.Active,
            onEvent = playerStateViewModel::onEvent,
        )
    }
}
