package com.andanana.musicplayer.feature.library.dialog

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andanana.musicplayer.core.designsystem.component.SmpTextButton
import com.andanana.musicplayer.core.designsystem.theme.MusicPlayerTheme

@Composable
fun NewPlayListDialog(
    onNavigateBack: () -> Unit,
    onCreateButtonClick: (name: String) -> Unit
) {
    NewPlayListDialogContent(
        onCreateButtonClick = onCreateButtonClick,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun NewPlayListDialogContent(
    modifier: Modifier = Modifier,
    onCreateButtonClick: (name: String) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    Card(
        modifier = modifier.wrapContentSize(),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(
            modifier = modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier.padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Create new playlist",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            var text by remember {
                mutableStateOf("")
            }
            val applyButtonEnabled by remember {
                derivedStateOf {
                    text.isNotEmpty()
                }
            }
            TextField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                value = text,
                onValueChange = { text = it },
                supportingText = {
                    Text(
                        text = "Please input playlist name.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))
                SmpTextButton(
                    onClick = onNavigateBack,
                    text = "Cancel"
                )
                Spacer(modifier = Modifier.width(10.dp))
                SmpTextButton(
                    onClick = {
                        onCreateButtonClick(text)
                        onNavigateBack.invoke()
                    },
                    enabled = applyButtonEnabled,
                    text = "Create"
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Preview
@Composable
private fun NewPlayListDialogContentPreview() {
    MusicPlayerTheme {
        NewPlayListDialogContent()
    }
}
