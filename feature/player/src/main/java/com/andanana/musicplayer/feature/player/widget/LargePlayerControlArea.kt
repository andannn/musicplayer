package com.andanana.musicplayer.feature.player.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.ShuffleOn
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andanana.musicplayer.core.data.model.PlayMode
import com.andanana.musicplayer.core.designsystem.component.SmpMainIconButton
import com.andanana.musicplayer.core.designsystem.component.SmpSubIconButton
import com.andanana.musicplayer.core.designsystem.theme.MusicPlayerTheme
import com.andanana.musicplayer.feature.player.PlayerUiEvent
import com.andanana.musicplayer.feature.player.util.getIcon

@Composable
fun LargePlayerControlArea(
    title: String,
    artist: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    progress: Float = 0.5f,
    isPlaying: Boolean = false,
    playMode: PlayMode = PlayMode.REPEAT_ALL,
    onEvent: (PlayerUiEvent) -> Unit = {},
    isShuffle: Boolean = false,
) {
    val titleState by rememberUpdatedState(newValue = title)
    Column(
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier.padding(horizontal = MaxImagePaddingStart),
            text = titleState,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            modifier = Modifier.padding(horizontal = MaxImagePaddingStart),
            text = artist,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(60.dp))
        Slider(
            value = progress,
            enabled = enabled,
            onValueChange = {
                onEvent(PlayerUiEvent.OnProgressChange(it))
            },
        )
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SmpSubIconButton(
                modifier =
                    Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                enabled = enabled,
                imageVector = if (isShuffle) Icons.Rounded.ShuffleOn else Icons.Rounded.Shuffle,
                onClick = {
                    onEvent(PlayerUiEvent.OnShuffleButtonClick)
                },
            )
            SmpSubIconButton(
                modifier =
                    Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                scale = 2f,
                enabled = enabled,
                imageVector = Icons.Rounded.SkipPrevious,
                onClick = {
                    onEvent(PlayerUiEvent.OnPreviousButtonClick)
                },
            )

            SmpMainIconButton(
                modifier =
                    Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                enabled = enabled,
                imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                onClick = {
                    onEvent(PlayerUiEvent.OnPlayButtonClick)
                },
            )
            SmpSubIconButton(
                modifier =
                    Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(10.dp),
                scale = 2f,
                imageVector = Icons.Rounded.SkipNext,
                enabled = enabled,
                onClick = {
                    onEvent(PlayerUiEvent.OnNextButtonClick)
                },
            )
            SmpSubIconButton(
                modifier = Modifier.weight(1f),
                imageVector = playMode.getIcon(),
                enabled = enabled,
                onClick = {
                    onEvent(PlayerUiEvent.OnPlayModeButtonClick)
                },
            )
        }
    }
}

@Preview
@Composable
private fun LargeControlAreaPreview() {
    MusicPlayerTheme(darkTheme = false) {
        Surface {
            LargePlayerControlArea(
                modifier = Modifier,
                title = "title",
                artist = "artist",
                enabled = true,
            )
        }
    }
}
