package com.andanana.musicplayer.core.designsystem.component

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import coil.compose.AsyncImage
import com.andanana.musicplayer.core.designsystem.theme.MusicPlayerTheme

private const val TAG = "BottomPlayerSheet"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomPlayerSheet(
    modifier: Modifier = Modifier,
    coverUri: String,
    isPlaying: Boolean = false,
    isFavorite: Boolean = false,
    title: String = "",
    artist: String = "",
    progress: Float = 1f,
    onPlayerSheetClick: () -> Unit = {},
    onPlayControlButtonClick: () -> Unit = {},
    onFavoriteButtonClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 50.dp, max = 70.dp),
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        shadowElevation = 10.dp,
        onClick = onPlayerSheetClick
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.padding(5.dp).weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Log.d(TAG, "BottomPlayerSheet: ")

                AsyncImage(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(shape = CircleShape)
                        .border(
                            shape = CircleShape,
                            border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.primary)
                        ),
                    model = coverUri,
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(10.dp))
                Row(
                    modifier = Modifier.padding(top = 15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = title,
                            maxLines = 1,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = artist,
                            maxLines = 1,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        modifier = Modifier.size(30.dp),
                        onClick = onPlayControlButtonClick
                    ) {
                        if (isPlaying) {
                            Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = "")
                        } else {
                            Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = "")
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    IconButton(
                        modifier = Modifier.size(30.dp),
                        onClick = onFavoriteButtonClick
                    ) {
                        if (isFavorite) {
                            Icon(
                                imageVector = Icons.Rounded.Favorite,
                                tint = Color.Red,
                                contentDescription = ""
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.FavoriteBorder,
                                contentDescription = ""
                            )
                        }
                    }
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(fraction = progress).height(3.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    )
            )
        }
    }
}

@Preview(name = "Dark")
@Composable
fun PlayingWithFavoriteSongBottomPlayerSheetPreview() {
    MusicPlayerTheme() {
        BottomPlayerSheet(
            coverUri = "",
            title = "Song name",
            artist = "Artist name",
            isFavorite = true,
            isPlaying = true
        )
    }
}

@Preview(name = "Dark")
@Composable
fun DarkBottomPlayerSheetPreview() {
    MusicPlayerTheme(darkTheme = true) {
        BottomPlayerSheet(
            coverUri = "",
            title = "Song name",
            artist = "Artist name"
        )
    }
}

@Preview(name = "Light")
@Composable
fun LightBottomPlayerSheetPreview() {
    MusicPlayerTheme(darkTheme = false) {
        BottomPlayerSheet(
            coverUri = "",
            title = "Song name",
            artist = "Artist name"
        )
    }
}
