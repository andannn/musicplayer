package com.andannn.musicplayer.common.drawer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andanana.musicplayer.core.designsystem.theme.MusicPlayerTheme
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.FlexibleSheetSize
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState

@Composable
fun MediaBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
) {
    FlexibleBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState =
            rememberFlexibleBottomSheetState(
                flexibleSheetSize =
                    FlexibleSheetSize(
                        fullyExpanded = 0.9f,
                        intermediatelyExpanded = 0.5f,
                        slightlyExpanded = 0.15f,
                    ),
                isModal = true,
                skipSlightlyExpanded = false,
            ),
        containerColor = Color.Black,
    ) {
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            text = "This is Flexible Bottom Sheet",
            textAlign = TextAlign.Center,
            color = Color.White,
        )
    }
}

@Preview
@Composable
private fun MediaBottomDrawerPreview() {
    MusicPlayerTheme {
        MediaBottomSheet()
    }
}
