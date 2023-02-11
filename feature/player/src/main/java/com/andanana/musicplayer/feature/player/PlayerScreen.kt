package com.andanana.musicplayer.feature.player

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NavigateBefore
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.andanana.musicplayer.core.designsystem.component.SmpSubIconButton
import com.andanana.musicplayer.core.player.PlayerStateViewModel
import com.andanana.musicplayer.core.player.PlayerUiState
import kotlin.math.roundToInt

@Composable
internal fun PlayerRoute(
    playerStateViewModel: PlayerStateViewModel = hiltViewModel()
) {
    val uiState by playerStateViewModel.playerUiStateFlow.collectAsState()
    playerScreen(
        uiState = uiState,
        onSeekToTime = playerStateViewModel::onSeekToTime
    )
}

@Composable
private fun playerScreen(
    uiState: PlayerUiState,
    onSeekToTime: (Int) -> Unit
) {
    when (uiState) {
        PlayerUiState.Inactive -> {
        }
        is PlayerUiState.Active -> {
            PlayerScreenContent(
                coverArtUri = uiState.musicInfo.albumUri,
                onOptionButtonClick = {},
                title = uiState.musicInfo.title,
                subTitle = uiState.musicInfo.album,
                progress = uiState.progress,
                duration = uiState.musicInfo.duration,
                onSeekToTime = onSeekToTime
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerScreenContent(
    modifier: Modifier = Modifier,
    progress: Float,
    coverArtUri: String,
    title: String,
    subTitle: String,
    duration: Int = 1000,
    onOptionButtonClick: () -> Unit = {},
    onSeekToTime: (Int) -> Unit = {}
) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                SmpSubIconButton(
                    onClick = onOptionButtonClick,
                    imageVector = Icons.Rounded.NavigateBefore
                )
                Spacer(modifier = Modifier.weight(1f))
                SmpSubIconButton(
                    onClick = onOptionButtonClick,
                    imageVector = Icons.Rounded.Share
                )
            }
            Spacer(modifier = Modifier.height(100.dp))
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
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
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = title,
                maxLines = 1,
                style = MaterialTheme.typography.headlineLarge,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = subTitle,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(10.dp))
            Slider(
                modifier = Modifier.padding(horizontal = 10.dp),
                value = progress,
                onValueChange = {
                    onSeekToTime.invoke(duration.times(it).roundToInt())
                }
            )
        }
    }
}

@Preview
@Composable
private fun PlayerScreenContentPreview() {
    PlayerScreenContent(
        coverArtUri = "",
        title = "Title",
        subTitle = "subTitle",
        progress = 0.7f
    )
}
