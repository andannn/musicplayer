package com.andanana.musicplayer.feature.player

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.andanana.musicplayer.core.designsystem.component.MiniPlayerBox

private const val TAG = "MiniPlayerBox"

@Composable
fun MiniPlayerBox(
    playerStateViewModel: PlayerStateViewModel = hiltViewModel(),
    onNavigateToPlayer: () -> Unit,
    onToggleFavorite: (Uri) -> Unit
) {
    val playerUiState by playerStateViewModel.playerUiStateFlow.collectAsState()

    MiniPlayerBoxContent(
        state = playerUiState,
        onPlayerSheetClick = onNavigateToPlayer,
        onPlayControlButtonClick = playerStateViewModel::togglePlayState,
        onFavoriteButtonClick = {
            (playerUiState as? PlayerUiState.Active)?.let {
                onToggleFavorite(it.musicModel.contentUri)
            }
        },
        onPlayNextButtonClick = playerStateViewModel::next
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
        MiniPlayerBox(
            modifier = modifier,
            coverUri = state.musicModel.albumUri.toString(),
            isPlaying = state.state == PlayState.PLAYING,
            isLoading = state.state == PlayState.LOADING,
            title = state.musicModel.title,
            artist = state.musicModel.artist,
            progress = state.progress,
            isFavorite = state.isFavorite,
            onPlayerSheetClick = onPlayerSheetClick,
            onPlayNextButtonClick = onPlayNextButtonClick,
            onPlayControlButtonClick = onPlayControlButtonClick,
            onFavoriteButtonClick = onFavoriteButtonClick
        )
    }
}
