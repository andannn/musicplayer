package com.andanana.musicplayer.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.text.SimpleDateFormat

private const val TAG = "MusicCard"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicCard(
    modifier: Modifier = Modifier,
    albumArtUri: String,
    title: String,
    artist: String,
    date: Long,
    onMusicItemClick: () -> Unit = {},
    onOptionButtonClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        onClick = onMusicItemClick
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(50.dp)
                    .clip(MaterialTheme.shapes.extraSmall),
                model = albumArtUri,
                contentDescription = ""
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = artist,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT)
                            .format(date),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            IconButton(
                modifier = Modifier,
                onClick = onOptionButtonClick
            ) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "menu")
            }
        }
    }
}

@Preview
@Composable
private fun MusicCardPreview() {
    MusicCard(
        albumArtUri = "",
        title = "Title",
        artist = "artist",
        date = 1543121980333L
    )
}
