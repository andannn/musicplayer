package com.andanana.musicplayer.core.designsystem.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.andanana.musicplayer.core.designsystem.theme.MusicPlayerTheme

val PlayBoxMaxHeight = 195.dp
val PlayBoxMinHeight = 120.dp

private const val TAG = "PlayListControlBox"

@Composable
fun PlayListControlBox(
    modifier: Modifier = Modifier,
    height: Dp,
    coverArtUri: String,
    title: String,
    trackCount: Int,
    onPlayAllButtonClick: () -> Unit = {},
    onAddToPlayListButtonClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.height(height).fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        BoxWithConstraints(modifier = Modifier) {
            val heightDp by rememberUpdatedState(newValue = maxHeight)
            val scaleRatio = remember(heightDp) {
                ((heightDp - PlayBoxMinHeight) / (PlayBoxMaxHeight - PlayBoxMinHeight)).let {
                    it.coerceIn(0f, 1f)
                }
            }
            Log.d(TAG, "PlayListControlBox: scaleRatio $scaleRatio")
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
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Box(
                        modifier = Modifier.alpha(scaleRatio).weight(1f).fillMaxHeight(scaleRatio)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.CenterStart),
                            text = "$trackCount tracks",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
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
                            alpha = (1f - scaleRatio),
                            onClick = onAddToPlayListButtonClick
                        )
                    }
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
                height = PlayBoxMinHeight,
                coverArtUri = "",
                title = "Title",
                trackCount = 10
            )
        }
    }
}
