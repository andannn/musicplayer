package com.andannn.musicplayer.common.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.andanana.musicplayer.core.designsystem.icons.SmpIcon
import com.andanana.musicplayer.core.designsystem.theme.MusicPlayerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaBottomSheet(
    bottomSheet: BottomSheet,
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope(),
    onDismissRequest: (SheetItem?) -> Unit = {},
) {
    val sheetState =
        rememberModalBottomSheetState()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            onDismissRequest.invoke(null)
        },
    ) {
        Surface(modifier = modifier.navigationBarsPadding()) {
            Column(Modifier.fillMaxWidth()) {
                bottomSheet.itemList.map { item ->
                    SheetItem(
                        item = item,
                        onClick = {
                            scope.launch {
                                sheetState.hide()
                                onDismissRequest.invoke(item)
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun SheetItem(
    item: SheetItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier
            .clickable(onClick = onClick)
            .padding(16.dp)
            .fillMaxWidth(),
    ) {
        SmpIcon(item.smpIcon)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = item.text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun SmpIcon(item: SmpIcon) {
    when (item) {
        is SmpIcon.ImageVectorIcon -> {
            Icon(imageVector = item.imageVector, contentDescription = "")
        }
    }
}

@Preview
@Composable
private fun MediaBottomDrawerPreview() {
    MusicPlayerTheme {
        var isShow by remember {
            mutableStateOf(false)
        }

        Surface {
            Button(onClick = { isShow = true }) {
                Text(text = "Show")
            }
        }

        if (isShow) {
            MediaBottomSheet(
                BottomSheet.MusicBottomSheet,
                onDismissRequest = {
                    isShow = false
                },
            )
        }
    }
}
