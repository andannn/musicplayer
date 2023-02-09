package com.andanana.musicplayer.feature.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.andanana.musicplayer.core.designsystem.component.BottomPlayerSheet
import com.andanana.musicplayer.core.player.PlayState
import com.andanana.musicplayer.core.player.PlayerStateViewModel
import com.andanana.musicplayer.core.player.PlayerUiState

@Composable
fun MiniPlayerBox(
    rootViewModelStoreOwner: ViewModelStoreOwner
) {
    val playerStateViewModel = hiltViewModel<PlayerStateViewModel>(rootViewModelStoreOwner)
    val playerUiState by playerStateViewModel.playerUiStateFlow.collectAsState()

    MiniPlayerBoxContent(
        state = playerUiState,
        onPlayerSheetClick = {
        },
        onPlayControlButtonClick = {
        },
        onFavoriteButtonClick = {
        }
    )
}

@Composable
private fun MiniPlayerBoxContent(
    modifier: Modifier = Modifier,
    state: PlayerUiState,
    onPlayerSheetClick: () -> Unit,
    onPlayControlButtonClick: () -> Unit,
    onFavoriteButtonClick: () -> Unit
) {
    if (state is PlayerUiState.Active) {
        BottomPlayerSheet(
            modifier = modifier,
            coverUri = state.musicInfo.albumUri,
            isPlaying = state.state == PlayState.PLAYING,
            title = state.musicInfo.title,
            artist = state.musicInfo.artist,
            progress = state.progress,
            onPlayerSheetClick = onPlayerSheetClick,
            onPlayControlButtonClick = onPlayControlButtonClick,
            onFavoriteButtonClick = onFavoriteButtonClick
        )
    }
}
