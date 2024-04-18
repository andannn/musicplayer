package com.andanana.musicplayer.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.andanana.musicplayer.feature.player.PlayerSheet
import com.andanana.musicplayer.feature.player.PlayerStateViewModel
import com.andanana.musicplayer.feature.player.PlayerUiEvent
import com.andanana.musicplayer.feature.player.PlayerUiState
import com.andanana.musicplayer.feature.player.widget.PlayerShrinkHeight
import com.andanana.musicplayer.navigation.SmpNavHost
import com.andannn.musicplayer.common.drawer.MediaBottomSheet

@Composable
fun SimpleMusicApp(
    modifier: Modifier = Modifier,
    playerStateViewModel: PlayerStateViewModel = hiltViewModel(),
) {
    Surface(modifier = modifier.fillMaxSize()) {
        val state by playerStateViewModel.playerUiStateFlow.collectAsState()
        val bottomSheetModel by playerStateViewModel.bottomSheetModel.collectAsState()

        SmpNavHostContainer(
            modifier = Modifier.fillMaxSize(),
        )

        if (state is PlayerUiState.Active) {
            PlayerSheet(
                state = state as PlayerUiState.Active,
                onEvent = playerStateViewModel::onEvent,
            )

            if (bottomSheetModel != null) {
                MediaBottomSheet(
                    bottomSheet = bottomSheetModel!!.bottomSheet,
                    onDismissRequest = {
                        playerStateViewModel.onEvent(PlayerUiEvent.OnDismissDrawerRequest(it))
                    },
                )
            }
        }
    }
}

@Composable
fun SmpNavHostContainer(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    Box(modifier = modifier) {
        SmpNavHost(
            modifier = Modifier.fillMaxWidth(),
            navHostController = navController,
            onBackPressed = navController::popBackStack,
        )
        Spacer(modifier = Modifier.navigationBarsPadding().height(PlayerShrinkHeight))
    }
}
