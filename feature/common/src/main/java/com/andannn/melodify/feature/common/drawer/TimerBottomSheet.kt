package com.andannn.melodify.feature.common.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andannn.melodify.feature.common.theme.MelodifyTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepTimerOptionBottomSheet(
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope(),
    onDismissRequest: (SleepTimerOption?) -> Unit = {},
) {
    val sheetState =
        rememberModalBottomSheetState()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            onDismissRequest.invoke(null)
        },
    ) {
        SleepTimerOptionSheetContent(
            onDismissRequest = {
                onDismissRequest.invoke(it)

            }
        )
    }
}

enum class SleepTimerOption(
    val timeMinutes: Duration?,
) {
    FIVE_MINUTES(5.minutes),
    FIFTEEN_MINUTES(15.minutes),
    THIRTY_MINUTES(30.minutes),
    SIXTY_MINUTES(60.minutes),
    SONG_FINISH(null),
}

@Composable
fun SleepTimerOptionSheetContent(
    modifier: Modifier = Modifier,
    onDismissRequest: (SleepTimerOption) -> Unit = {},
) {
    Surface(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = "SleepTimer",
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(modifier = Modifier.height(3.dp))

            SleepTimerOption.entries.forEach {
                OptionItem(
                    text = if (it != SleepTimerOption.SONG_FINISH) it.timeMinutes.toString() else "when song finish",
                    onClick = {
                        onDismissRequest.invoke(it)
                    },
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun OptionItem(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .fillMaxWidth(),
    ) {
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
private fun TimerSheetPreview() {
    MelodifyTheme {
        SleepTimerOptionSheetContent(

        )
    }
}

@Preview
@Composable
private fun MediaBottomDrawerDemo() {
    MelodifyTheme {
        var isShow by remember {
            mutableStateOf(false)
        }

        Surface {
            Button(onClick = { isShow = true }) {
                Text(text = "Show")
            }
        }

        if (isShow) {
            SleepTimerOptionBottomSheet(
                onDismissRequest = {
                    isShow = false
                },
            )
        }
    }
}
