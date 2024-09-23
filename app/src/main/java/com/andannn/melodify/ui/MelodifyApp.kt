package com.andannn.melodify.ui

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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.andannn.melodify.MainActivityViewModel
import com.andannn.melodify.common.drawer.BottomSheet
import com.andannn.melodify.feature.player.PlayerSheet
import com.andannn.melodify.feature.player.PlayerStateViewModel
import com.andannn.melodify.feature.player.PlayerUiEvent
import com.andannn.melodify.feature.player.PlayerUiState
import com.andannn.melodify.feature.player.widget.PlayerShrinkHeight
import com.andannn.melodify.navigation.SmpNavHost
import com.andannn.melodify.common.drawer.MediaBottomSheet
import com.andannn.melodify.common.drawer.SheetItem

@Composable
fun MelodifyApp(
    modifier: Modifier = Modifier,
    playerStateViewModel: PlayerStateViewModel = hiltViewModel(),
    mainViewModel: MainActivityViewModel = hiltViewModel(),
) {
    Surface(modifier = modifier.fillMaxSize()) {
        val state by playerStateViewModel.playerUiStateFlow.collectAsState()
        val bottomSheetModel by mainViewModel.bottomSheetModel.collectAsState()

        SmpNavHostContainer(
            modifier = Modifier.fillMaxSize(),
        )

        if (state is PlayerUiState.Active) {
            PlayerSheet(
                state = state as PlayerUiState.Active,
                onEvent = playerStateViewModel::onEvent,
            )
        }

        BottomSheetContainer(
            bottomSheet = bottomSheetModel?.bottomSheet,
            onDismissRequest = {
                playerStateViewModel.onEvent(PlayerUiEvent.OnDismissDrawerRequest(it))
            }
        )
    }
}

@Composable
private fun BottomSheetContainer(
    bottomSheet: BottomSheet?,
    onDismissRequest: (SheetItem?) -> Unit = {},
) {
    val onDismissState = rememberUpdatedState(onDismissRequest)
    if (bottomSheet != null) {
        MediaBottomSheet(
            bottomSheet = bottomSheet,
            onDismissRequest = {
                onDismissState.value(it)
            },
        )
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
        Spacer(
            modifier = Modifier
                .navigationBarsPadding()
                .height(PlayerShrinkHeight)
        )
    }
}
