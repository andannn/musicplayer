package com.andanana.musicplayer.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.andanana.musicplayer.core.designsystem.theme.MusicPlayerTheme

private val PlayBoxMaxHeight = 240.dp
private val PlayBoxMinHeight = 170.dp

@Composable
fun PlayListControlBox(
    modifier: Modifier = Modifier,
    coverArtUri: String,
    title: String,
    trackCount: Int,
    onPlayAllButtonClick: () -> Unit = {},
    onAddToPlayListButtonClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .heightIn(min = PlayBoxMinHeight, max = PlayBoxMaxHeight)
            .padding(20.dp).then(modifier),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(modifier = Modifier.padding(15.dp)) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.extraSmall),
                model = coverArtUri,
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    maxLines = 1,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "$trackCount tracks",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(modifier = Modifier) {
                    SmpTextButton(
                        imageVector = Icons.Rounded.PlayArrow,
                        text = "Play all",
                        onClick = onPlayAllButtonClick
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    SmpTextButton(
                        imageVector = Icons.Rounded.AddCircle,
                        text = "Add to..",
                        onClick = onAddToPlayListButtonClick
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PlayListControlBoxPreview() {
    MusicPlayerTheme {
        Surface {
            PlayListControlBox(
                modifier = Modifier.height(500.dp),
                coverArtUri = "",
                title = "Title",
                trackCount = 10
            )
        }
    }
}
