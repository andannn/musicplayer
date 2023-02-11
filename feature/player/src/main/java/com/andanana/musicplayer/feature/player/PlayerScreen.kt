package com.andanana.musicplayer.feature.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.andanana.musicplayer.core.player.PlayerStateViewModel

@Composable
internal fun PlayerScreen(
    playerStateViewModel: PlayerStateViewModel = hiltViewModel()
) {
    playerScreenContent()
}

@Composable
private fun playerScreenContent() {
    Box(modifier = Modifier.fillMaxSize().background(Color.Yellow)) {
        Text(text = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
    }
}
