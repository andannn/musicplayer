package com.andanana.musicplayer.feature.player

import androidx.compose.runtime.Composable
import com.andanana.musicplayer.feature.player.widget.ShrinkablePlayBox

@Composable
fun PlayerSheet(
    state: PlayerUiState.Active,
    onEvent: (PlayerUiEvent) -> Unit,
) {
    ShrinkablePlayBox(
        state = state,
        onEvent = onEvent,
    )
}
