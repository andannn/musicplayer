package com.andannn.melodify.feature.player.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andannn.melodify.core.designsystem.R
import com.andannn.melodify.core.designsystem.theme.MusicPlayerTheme
import com.andannn.melodify.feature.player.PlayerUiEvent

@Composable
fun MiniPlayerLayout(
    title: String,
    artist: String,
    isPlaying: Boolean,
    isFavorite: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onEvent: (PlayerUiEvent) -> Unit = {},
) {
// TODO: It seems that there is a bug: Recomposition is done but ui is not update.
//       I can not tell why this happened.
//       Use <rememberUpdatedState> as a workaround.
    val titleState = rememberUpdatedState(title)
    val artistState = rememberUpdatedState(artist)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = titleState.value,
                maxLines = 1,
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = artistState.value,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        IconButton(
            modifier =
                Modifier
                    .size(30.dp)
                    .scale(1.2f),
            enabled = enabled,
            onClick = {
                onEvent(PlayerUiEvent.OnPlayButtonClick)
            },
        ) {
            if (isPlaying) {
                Icon(imageVector = Icons.Rounded.Pause, contentDescription = "")
            } else {
                Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = "")
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        IconButton(
            modifier =
                Modifier
                    .size(30.dp)
                    .padding(5.dp)
                    .rotate(180f),
            enabled = enabled,
            onClick = {
                onEvent(PlayerUiEvent.OnNextButtonClick)
            },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.music_music_player_player_previous_icon),
                contentDescription = "",
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        IconButton(
            modifier = Modifier.size(30.dp),
            enabled = enabled,
            onClick = {
                onEvent(PlayerUiEvent.OnFavoriteButtonClick)
            },
        ) {
            if (isFavorite) {
                Icon(
                    imageVector = Icons.Rounded.Favorite,
                    tint = Color.Red,
                    contentDescription = "",
                )
            } else {
                Icon(
                    imageVector = Icons.Rounded.FavoriteBorder,
                    contentDescription = "",
                )
            }
        }
        Spacer(modifier = Modifier.width(5.dp))
    }
}

@Preview
@Composable
private fun MiniPlayerLayoutPreview() {
    MusicPlayerTheme{
        Surface {
            MiniPlayerLayout(
                modifier = Modifier.fillMaxWidth().height(PlayerShrinkHeight),
                title = "BBBBB",
                artist = "AAAAA",
                isPlaying = true,
                isFavorite = false,
                enabled = true,
            )
        }
    }
}
