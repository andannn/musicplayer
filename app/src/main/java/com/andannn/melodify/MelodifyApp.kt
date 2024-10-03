package com.andannn.melodify

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
import com.andannn.melodify.feature.common.UiEvent
import com.andannn.melodify.feature.player.PlayerStateViewModel
import com.andannn.melodify.feature.player.PlayerUiState
import com.andannn.melodify.feature.player.ui.ShrinkPlayerHeight
import com.andannn.melodify.navigation.SmpNavHost
import com.andannn.melodify.feature.common.drawer.MediaOptionBottomSheet
import com.andannn.melodify.feature.common.drawer.SheetModel
import com.andannn.melodify.feature.common.drawer.SleepTimerCountingBottomSheet
import com.andannn.melodify.feature.common.drawer.SleepTimerOptionBottomSheet
import com.andannn.melodify.feature.player.PlayerAreaView

@Composable
fun MelodifyApp(
    modifier: Modifier = Modifier,
    playerStateViewModel: PlayerStateViewModel = hiltViewModel(),
    mainViewModel: MainActivityViewModel = hiltViewModel(),
) {
    Surface(modifier = modifier.fillMaxSize()) {
        val state by playerStateViewModel.playerUiStateFlow.collectAsState()
        val bottomSheetModel by mainViewModel.bottomSheetModel.collectAsState(null)

        SmpNavHostContainer(
            modifier = Modifier.fillMaxSize(),
        )

        if (state is PlayerUiState.Active) {
            PlayerAreaView(
                state = state as PlayerUiState.Active,
                onEvent = playerStateViewModel::onEvent,
            )
        }

        BottomSheetContainer(
            bottomSheet = bottomSheetModel,
            onEvent = { event ->
                mainViewModel.onRequestDismissSheet(event)
            }
        )
    }
}

@Composable
private fun BottomSheetContainer(
    bottomSheet: SheetModel?,
    onEvent: (UiEvent) -> Unit = {},
) {
    if (bottomSheet != null) {
        when (bottomSheet) {
            is SheetModel.MediaOptionSheet -> {
                MediaOptionBottomSheet(
                    optionSheet = bottomSheet,
                    onClickOption = {
                        onEvent(UiEvent.OnMediaOptionClick(bottomSheet, it))
                    },
                    onRequestDismiss = {
                        onEvent(UiEvent.OnDismissSheet(bottomSheet))
                    }
                )
            }

            SheetModel.TimerOptionSheet -> {
                SleepTimerOptionBottomSheet(
                    onSelectOption = {
                        onEvent(UiEvent.OnTimerOptionClick(it))
                    },
                    onRequestDismiss = {
                        onEvent(UiEvent.OnDismissSheet(bottomSheet))
                    }
                )
            }

            is SheetModel.TimerRemainTimeSheet -> {
                SleepTimerCountingBottomSheet(
                    remain = bottomSheet.remainTime,
                    onCancelTimer = {
                        onEvent(UiEvent.OnCancelTimer)
                    },
                    onRequestDismiss = {
                        onEvent(UiEvent.OnDismissSheet(bottomSheet))
                    }
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
        Spacer(
            modifier = Modifier
                .navigationBarsPadding()
                .height(ShrinkPlayerHeight)
        )
    }
}
