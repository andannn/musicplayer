package com.andannn.melodify.feature.player

import androidx.compose.runtime.Composable
import com.andannn.melodify.feature.player.widget.ShrinkablePlayBox

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
