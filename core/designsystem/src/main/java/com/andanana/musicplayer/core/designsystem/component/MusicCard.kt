package com.andanana.musicplayer.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
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

private const val TAG = "MusicCard"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicCard(
    modifier: Modifier = Modifier,
    albumArtUri: String,
    isActive: Boolean = false,
    title: String,
    artist: String,
    trackNum: Int = 0,
    showTrackNum: Boolean = false,
    showSwapIcon: Boolean = false,
    onMusicItemClick: () -> Unit = {},
    onOptionButtonClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = if (isActive) {
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inversePrimary)
        } else {
            CardDefaults.cardColors()
        },
        shape = MaterialTheme.shapes.medium,
        onClick = onMusicItemClick
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier) {
                if (showTrackNum) {
                    Text(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.inverseOnSurface,
                                shape = MaterialTheme.shapes.extraSmall
                            )
                            .align(Alignment.Center)
                            .padding(horizontal = 10.dp),
                        text = trackNum.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    AsyncImage(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(MaterialTheme.shapes.extraSmall),
                        model = albumArtUri,
                        contentDescription = ""
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = artist,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (showSwapIcon) {
                IconButton(
                    modifier = Modifier,
                    onClick = onOptionButtonClick
                ) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "menu")
                }
            } else {
                IconButton(
                    modifier = Modifier,
                    onClick = onOptionButtonClick
                ) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "menu")
                }
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
        showTrackNum = true,
        showSwapIcon = true
    )
}
