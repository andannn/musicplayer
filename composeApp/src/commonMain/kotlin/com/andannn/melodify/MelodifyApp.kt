package com.andannn.melodify

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.andannn.melodify.feature.common.GlobalUiController
import com.andannn.melodify.feature.common.UiEvent
import com.andannn.melodify.feature.player.PlayerStateViewModel
import com.andannn.melodify.feature.player.PlayerUiState
import com.andannn.melodify.feature.player.ui.ShrinkPlayerHeight
import com.andannn.melodify.navigation.MelodifyNavHost
import com.andannn.melodify.feature.common.drawer.MediaOptionBottomSheet
import com.andannn.melodify.feature.common.drawer.SheetModel
import com.andannn.melodify.feature.common.drawer.SleepTimerCountingBottomSheet
import com.andannn.melodify.feature.common.drawer.SleepTimerOptionBottomSheet
import com.andannn.melodify.feature.player.PlayerAreaView
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MelodifyApp(
    modifier: Modifier = Modifier,
    playerStateViewModel: PlayerStateViewModel = koinViewModel(),
    controller: GlobalUiController = koinInject(),
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        val state by playerStateViewModel.playerUiStateFlow.collectAsState()

        SmpNavHostContainer(
            modifier = Modifier.fillMaxSize(),
        )

        if (state is PlayerUiState.Active) {
            PlayerAreaView(
                state = state as PlayerUiState.Active,
                onEvent = playerStateViewModel::onEvent,
            )
        }

        val bottomSheetModel by controller.bottomSheetModel.collectAsState(null)
        val scope = rememberCoroutineScope()
        BottomSheetContainer(
            bottomSheet = bottomSheetModel,
            onEvent = { event ->
                scope.launch {
                    controller.onEvent(event)
                }
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
        MelodifyNavHost(
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
