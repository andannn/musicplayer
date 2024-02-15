package com.andanana.musicplayer.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.andanana.musicplayer.core.designsystem.theme.MusicPlayerTheme

@Composable
fun PlayListHeader(
    modifier: Modifier = Modifier,
    coverArtUri: String = "",
    title: String = "",
    trackCount: Int = 0,
    onPlayAllButtonClick: () -> Unit = {},
    onAddToPlayListButtonClick: () -> Unit = {},
) {
    Card(
        modifier = modifier.fillMaxWidth().wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(modifier = Modifier.padding(5.dp)) {
            AsyncImage(
                modifier =
                    Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(MaterialTheme.shapes.extraSmall),
                model = coverArtUri,
                contentDescription = "",
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier =
                    Modifier.weight(1f),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    modifier = Modifier,
                    text = "$trackCount tracks",
                    style = MaterialTheme.typography.bodySmall,
                )
                Spacer(modifier = Modifier.height(15.dp))
                Row(modifier = Modifier) {
                    SmpTextButton(
                        imageVector = Icons.Rounded.PlayArrow,
                        onClick = onPlayAllButtonClick,
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    SmpTextButton(
                        imageVector = Icons.Rounded.Shuffle,
                        onClick = onAddToPlayListButtonClick,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PlayListControlBoxPreview() {
    MusicPlayerTheme {
        Surface {
            PlayListHeader(
                coverArtUri = "",
                title = "Title",
                trackCount = 10,
            )
        }
    }
}

@Preview
@Composable
private fun PlayListControlBoxLongTitlePreview() {
    MusicPlayerTheme {
        Surface {
            PlayListHeader(
                coverArtUri = "",
                title = "Title Title Title Title Title Title Title Title Title Title Title Title Title Title Title Title ",
                trackCount = 10,
            )
        }
    }
}
