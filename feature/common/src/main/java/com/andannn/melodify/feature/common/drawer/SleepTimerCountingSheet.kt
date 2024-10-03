package com.andannn.melodify.feature.common.drawer

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andannn.melodify.feature.common.theme.MelodifyTheme
import kotlinx.coroutines.CoroutineScope
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepTimerCountingBottomSheet(
    remain: Duration,
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope(),
    onRequestDismiss: () -> Unit = {},
    onCancelTimer: () -> Unit = {},
) {
    val sheetState =
        rememberModalBottomSheetState()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            onRequestDismiss.invoke()
        },
    ) {
        SleepTimerCounterSheetContent(
            remain = remain,
            onClickClose = {
                onRequestDismiss.invoke()
            },
            onClickCancel = {
                onCancelTimer.invoke()
            }
        )
    }
}

@Composable
private fun SleepTimerCounterSheetContent(
    remain: Duration,
    modifier: Modifier = Modifier,
    onClickClose: () -> Unit = {},
    onClickCancel: () -> Unit = {}
) {
    Surface(modifier = modifier) {
        Column {
            Text(remain.toString())

            IconButton(onClick = onClickCancel) {
                Icon(Icons.Sharp.Close, "")
            }
        }
    }
}


@Preview
@Composable
private fun SleepTimerCounterSheetContentPreview() {
    MelodifyTheme {
        SleepTimerCounterSheetContent(
            remain = 2.minutes
        )
    }
}