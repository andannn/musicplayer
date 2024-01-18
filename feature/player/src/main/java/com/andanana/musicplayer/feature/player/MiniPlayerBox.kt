package com.andanana.musicplayer.feature.player

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.andanana.musicplayer.core.designsystem.component.MiniPlayerBox

private const val TAG = "MiniPlayerBox"

@Composable
fun MiniPlayerBox(
    modifier: Modifier = Modifier,
    playerStateViewModel: PlayerStateViewModel = hiltViewModel(),
    onNavigateToPlayer: () -> Unit,
    onToggleFavorite: (Uri) -> Unit,
) {
    val playerUiState by playerStateViewModel.playerUiStateFlow.collectAsState()

    MiniPlayerBoxContent(
        modifier = modifier,
        state = playerUiState,
        onPlayerSheetClick = onNavigateToPlayer,
        onPlayControlButtonClick = playerStateViewModel::togglePlayState,
        onFavoriteButtonClick = {
//            (playerUiState as? PlayerUiState.Active)?.let {
//                onToggleFavorite(it.mediaItem.contentUri)
//            }
        },
        onPlayNextButtonClick = playerStateViewModel::next,
    )
}

@Composable
private fun MiniPlayerBoxContent(
    modifier: Modifier = Modifier,
    state: PlayerUiState,
    onPlayerSheetClick: () -> Unit = {},
    onPlayControlButtonClick: () -> Unit = {},
    onFavoriteButtonClick: () -> Unit = {},
    onPlayNextButtonClick: () -> Unit = {},
) {
    if (state is PlayerUiState.Active) {
        MiniPlayerBox(
            modifier = modifier,
            coverUri = state.mediaItem.mediaMetadata.artworkUri.toString(),
            isPlaying = state.state == PlayState.PLAYING,
            title = state.mediaItem.mediaMetadata.title.toString(),
            artist = state.mediaItem.mediaMetadata.artist.toString(),
            progress = state.progress,
            isFavorite = state.isFavorite,
            onPlayerSheetClick = onPlayerSheetClick,
            onPlayNextButtonClick = onPlayNextButtonClick,
            onPlayControlButtonClick = onPlayControlButtonClick,
            onFavoriteButtonClick = onFavoriteButtonClick,
        )
    }
}
