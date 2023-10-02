package com.andanana.musicplayer.feature.player

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.PlaylistPlay
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.andanana.musicplayer.core.designsystem.R
import com.andanana.musicplayer.core.designsystem.component.SmpMainIconButton
import com.andanana.musicplayer.core.designsystem.component.SmpSubIconButton
import com.andanana.musicplayer.core.data.model.PlayMode
import com.andanana.musicplayer.feature.player.util.getIconRes
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

@Composable
fun PlayerRoute(
    playerStateViewModel: PlayerStateViewModel = hiltViewModel(),
    onNavigateToPlayQueue: () -> Unit
) {
    val uiState by playerStateViewModel.playerUiStateFlow.collectAsState()
    PlayerScreen(
        uiState = uiState,
        onSeekToTime = playerStateViewModel::onSeekToTime,
        onPlayButtonClick = playerStateViewModel::togglePlayState,
        onPreviousButtonClick = playerStateViewModel::previous,
        onNextButtonClick = playerStateViewModel::next,
        onPlayModeButtonClick = playerStateViewModel::changePlayMode,
        onPlayListButtonClick = onNavigateToPlayQueue,
    )
}

@Composable
private fun PlayerScreen(
    uiState: PlayerUiState,
    onSeekToTime: (Int) -> Unit = {},
    onPlayButtonClick: () -> Unit = {},
    onPreviousButtonClick: () -> Unit = {},
    onNextButtonClick: () -> Unit = {},
    onPlayModeButtonClick: () -> Unit = {},
    onPlayListButtonClick: () -> Unit = {},
) {
    when (uiState) {
        PlayerUiState.Inactive -> {
        }
        is PlayerUiState.Active -> {
            PlayerScreenContent(
                progress = uiState.progress,
                playMode = uiState.playMode,
                isPlaying = uiState.state == PlayState.PLAYING,
                isLoading = uiState.state == PlayState.LOADING,
                coverArtUri = uiState.musicModel.albumUri.toString(),
                title = uiState.musicModel.title,
                subTitle = uiState.musicModel.album,
                duration = uiState.musicModel.duration,
                onSeekToTime = onSeekToTime,
                onPlayButtonClick = onPlayButtonClick,
                onPreviousButtonClick = onPreviousButtonClick,
                onNextButtonClick = onNextButtonClick,
                onPlayModeButtonClick = onPlayModeButtonClick,
                onPlayListButtonClick = onPlayListButtonClick,
            )
        }
    }
}

@Composable
private fun PlayerScreenContent(
    modifier: Modifier = Modifier,
    progress: Float,
    playMode: PlayMode,
    isPlaying: Boolean,
    isLoading: Boolean,
    coverArtUri: String,
    title: String,
    subTitle: String,
    duration: Int = 1000,
    onSeekToTime: (Int) -> Unit = {},
    onPlayButtonClick: () -> Unit = {},
    onPreviousButtonClick: () -> Unit = {},
    onPlayModeButtonClick: () -> Unit = {},
    onNextButtonClick: () -> Unit = {},
    onPlayListButtonClick: () -> Unit = {},
) {
    Surface(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .border(
                        shape = CircleShape,
                        border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.primary)
                    )
                    .align(CenterHorizontally),
                model = coverArtUri,
                contentScale = ContentScale.FillHeight,
                contentDescription = ""
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = title,
                maxLines = 1,
                style = MaterialTheme.typography.headlineSmall,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = subTitle,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(10.dp))
            Slider(
                modifier = Modifier.padding(horizontal = 30.dp),
                value = progress,
                onValueChange = {
                    onSeekToTime.invoke(duration.times(it).roundToInt())
                }
            )
            Row(modifier = Modifier.padding(horizontal = 30.dp)) {
                val formatter = remember {
                    SimpleDateFormat("mm:ss")
                }
                val durationString = formatter.format(duration)
                val currentString = formatter.format(duration.times(progress).roundToInt())
                Text(text = currentString, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = durationString, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.padding(horizontal = 30.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SmpSubIconButton(
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    onClick = onPlayModeButtonClick,
                    resId = playMode.getIconRes()
                )
                SmpSubIconButton(
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    onClick = onPreviousButtonClick,
                    painter = painterResource(id = R.drawable.music_music_player_player_previous_icon)
                )
                val image = if (isPlaying) {
                    Icons.Rounded.Pause
                } else {
                    Icons.Rounded.PlayArrow
                }
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .weight(1f)
                            .padding(10.dp)
                            .aspectRatio(1f).clip(CircleShape)
                    )
                } else {
                    SmpMainIconButton(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        onClick = onPlayButtonClick,
                        imageVector = image
                    )
                }
                SmpSubIconButton(
                    modifier = Modifier
                        .weight(1f)
                        .rotate(180f)
                        .aspectRatio(1f)
                        .padding(10.dp),
                    onClick = onNextButtonClick,
                    painter = painterResource(id = R.drawable.music_music_player_player_previous_icon)
                )
                SmpSubIconButton(
                    modifier = Modifier.weight(1f),
                    onClick = onPlayListButtonClick,
                    imageVector = Icons.Rounded.PlaylistPlay
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Preview
@Composable
private fun PlayerScreenContentPreview() {
    PlayerScreenContent(
        progress = 0.7f,
        isPlaying = false,
        isLoading = true,
        coverArtUri = "",
        title = "Title",
        subTitle = "subTitle",
        playMode = PlayMode.SHUFFLE
    )
}
