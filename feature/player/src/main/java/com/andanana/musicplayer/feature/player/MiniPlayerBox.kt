package com.andanana.musicplayer.feature.player

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.andanana.musicplayer.core.designsystem.component.BottomPlayerSheet
import com.andanana.musicplayer.core.player.PlayState
import com.andanana.musicplayer.core.player.PlayerStateViewModel
import com.andanana.musicplayer.core.player.PlayerUiState

private const val TAG = "MiniPlayerBox"

@Composable
fun MiniPlayerBox(
    playerStateViewModel: PlayerStateViewModel,
    onNavigateToPlayer: () -> Unit
) {
    val playerUiState by playerStateViewModel.playerUiStateFlow.collectAsState()

    MiniPlayerBoxContent(
        state = playerUiState,
        onPlayerSheetClick = onNavigateToPlayer,
        onPlayControlButtonClick = playerStateViewModel::togglePlayState,
        onFavoriteButtonClick = {},
        onPlayNextButtonClick = playerStateViewModel::onPlayNextButtonClick
    )
}

@Composable
private fun MiniPlayerBoxContent(
    modifier: Modifier = Modifier,
    state: PlayerUiState,
    onPlayerSheetClick: () -> Unit = {},
    onPlayControlButtonClick: () -> Unit = {},
    onFavoriteButtonClick: () -> Unit = {},
    onPlayNextButtonClick: () -> Unit = {}
) {
    Log.d(TAG, "MiniPlayerBoxContent:$state")
    if (state is PlayerUiState.Active) {
        BottomPlayerSheet(
            modifier = modifier,
            coverUri = state.musicInfo.albumUri,
            isPlaying = state.state == PlayState.PLAYING,
            isLoading = state.state == PlayState.LOADING,
            title = state.musicInfo.title,
            artist = state.musicInfo.artist,
            progress = state.progress,
            onPlayerSheetClick = onPlayerSheetClick,
            onPlayNextButtonClick = onPlayNextButtonClick,
            onPlayControlButtonClick = onPlayControlButtonClick,
            onFavoriteButtonClick = onFavoriteButtonClick
        )
    }
}
