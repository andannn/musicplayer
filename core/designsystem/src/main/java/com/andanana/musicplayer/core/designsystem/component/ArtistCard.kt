package com.andanana.musicplayer.core.designsystem.component

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistCard(
    modifier: Modifier = Modifier,
    artistUri: Uri,
    name: String,
    trackCount: Int,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.extraSmall),
                model = artistUri,
                contentDescription = ""
            )
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "$trackCount Tracks",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
private fun ArtistCardPreview() {
    MusicPlayerTheme {
        Surface {
            AlbumCard(albumArtUri = Uri.parse(""), title = "Name", trackCount = 3)
        }
    }
}
