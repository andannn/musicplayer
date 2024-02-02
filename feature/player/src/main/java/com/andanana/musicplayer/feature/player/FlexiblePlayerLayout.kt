package com.andanana.musicplayer.feature.player

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.ShuffleOn
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import coil.compose.AsyncImage
import com.andanana.musicplayer.core.data.model.PlayMode
import com.andanana.musicplayer.core.designsystem.R
import com.andanana.musicplayer.core.designsystem.component.SmpMainIconButton
import com.andanana.musicplayer.core.designsystem.component.SmpSubIconButton
import com.andanana.musicplayer.core.designsystem.theme.MusicPlayerTheme

private const val TAG = "BottomPlayerSheet"

val MinImageSize = 60.dp
// MaxImageSize is calculated.

val MinImagePaddingTop = 5.dp
val MaxImagePaddingTop = 130.dp

val MinImagePaddingStart = 5.dp
val MaxImagePaddingStart = 20.dp

val MinFadeoutWithExpandAreaPaddingTop = 15.dp

@Composable
fun FlexiblePlayerLayout(
    modifier: Modifier = Modifier,
    heightPxRange: ClosedFloatingPointRange<Float> = 100f..800f,
    coverUri: String,
    isPlaying: Boolean = false,
    isFavorite: Boolean = false,
    title: String = "",
    artist: String = "",
    progress: Float = 1f,
    onEvent: (PlayerUiEvent) -> Unit = {},
    onShrinkButtonClick: () -> Unit = {},
) {
    val statusBarHeight =
        with(LocalDensity.current) {
            WindowInsets.statusBars.getTop(this).toDp()
        }
    val coverUriState = rememberUpdatedState(newValue = coverUri)

    Surface(
        modifier =
            modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        shadowElevation = 10.dp,
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
        ) {
            val maxImageWidth = maxWidth - MaxImagePaddingStart * 2
            val minImageWidth = MinImageSize

            val (minHeightPx, maxHeightPx) = heightPxRange.start to heightPxRange.endInclusive
            val currentHeight = constraints.maxHeight
            val expandFactor =
                (currentHeight - minHeightPx).div(maxHeightPx - minHeightPx).coerceIn(0f, 1f)
            Log.d(TAG, "FlexiblePlayerLayout: expandFactor $expandFactor")

            val imageWidthDp = lerp(start = minImageWidth, stop = maxImageWidth, expandFactor)
            val imagePaddingTopDp =
                lerp(start = MinImagePaddingTop, stop = MaxImagePaddingTop, expandFactor)
            val imagePaddingStartDp =
                lerp(start = MinImagePaddingStart, stop = MaxImagePaddingStart, expandFactor)
            val fadingAreaPaddingTop =
                lerp(
                    start = MinFadeoutWithExpandAreaPaddingTop,
                    stop = statusBarHeight,
                    expandFactor,
                )

            val fadeoutAreaAlpha = 1 - (expandFactor * 4).coerceIn(0f, 1f)
            val isExpand = expandFactor > 0.01f
            FadeoutWithExpandArea(
                modifier =
                    Modifier
                        .graphicsLayer {
                            alpha = fadeoutAreaAlpha
                        }
                        .fillMaxWidth()
                        .padding(
                            top = fadingAreaPaddingTop,
                            start = MinImagePaddingStart * 2 + MinImageSize,
                        ),
                title = title,
                artist = artist,
                isPlaying = isPlaying,
                isFavorite = isFavorite,
                onEvent = onEvent,
            )

            val fadeInAreaAlpha = (1f - (1f - expandFactor).times(3f)).coerceIn(0f, 1f)
            IconButton(
                modifier =
                    Modifier
                        .padding(top = statusBarHeight, start = 4.dp)
                        .rotate(-90f)
                        .graphicsLayer {
                            alpha = fadeInAreaAlpha
                        },
                onClick = onShrinkButtonClick,
            ) {
                Icon(imageVector = Icons.Rounded.ArrowBackIos, contentDescription = "Shrink")
            }
            IconButton(
                modifier =
                    Modifier
                        .padding(top = statusBarHeight, end = 4.dp)
                        .align(Alignment.TopEnd)
                        .graphicsLayer {
                            alpha = fadeInAreaAlpha
                        },
                onClick = {
                    onEvent(PlayerUiEvent.OnOptionIconClick)
                },
            ) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Menu")
            }

            Column {
                CircleImage(
                    modifier =
                        Modifier
                            .padding(top = imagePaddingTopDp)
                            .padding(start = imagePaddingStartDp)
                            .width(imageWidthDp)
                            .aspectRatio(1f),
                    model = coverUriState.value,
                )

                FadeInWithExpandArea(
                    modifier =
                        Modifier
                            .weight(1f)
                            .graphicsLayer {
                                alpha = fadeInAreaAlpha
                            }
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max),
                    isPlaying = isPlaying,
                    progress = progress,
                    title = title,
                    artist = artist,
                    onEvent = onEvent,
                )
            }

            if (!isExpand) {
                Spacer(
                    modifier =
                        Modifier
                            .fillMaxWidth(fraction = progress)
                            .align(Alignment.BottomStart)
                            .height(3.dp)
                            .background(
                                brush =
                                    Brush.horizontalGradient(
                                        colors =
                                            listOf(
                                                MaterialTheme.colorScheme.tertiaryContainer,
                                                MaterialTheme.colorScheme.inversePrimary,
                                                MaterialTheme.colorScheme.primary,
                                            ),
                                    ),
                            ),
                )
            }
        }
    }
}

@Composable
private fun FadeoutWithExpandArea(
    modifier: Modifier = Modifier,
    title: String,
    artist: String,
    isPlaying: Boolean,
    isFavorite: Boolean,
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

@Composable
private fun FadeInWithExpandArea(
    modifier: Modifier = Modifier,
    title: String,
    artist: String,
    progress: Float = 0.5f,
    isPlaying: Boolean = false,
    playMode: PlayMode = PlayMode.REPEAT_ALL,
    onEvent: (PlayerUiEvent) -> Unit = {},
) {
    val titleState by rememberUpdatedState(newValue = title)
    Column(
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier.padding(horizontal = MaxImagePaddingStart),
            text = titleState,
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
                onClick = {
                    onEvent(PlayerUiEvent.OnShuffleButtonClick)
                },
                imageVector = Icons.Rounded.ShuffleOn,
            )
            SmpSubIconButton(
                modifier =
                    Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                scale = 2f,
                onClick = {
                    onEvent(PlayerUiEvent.OnPreviousButtonClick)
                },
                imageVector = Icons.Rounded.SkipPrevious,
            )

            SmpMainIconButton(
                modifier =
                    Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                onClick = {
                    onEvent(PlayerUiEvent.OnPlayButtonClick)
                },
                imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
            )
            SmpSubIconButton(
                modifier =
                    Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(10.dp),
                scale = 2f,
                onClick = {
                    onEvent(PlayerUiEvent.OnNextButtonClick)
                },
                imageVector = Icons.Rounded.SkipNext,
            )
            SmpSubIconButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    onEvent(PlayerUiEvent.OnPlayModeButtonClick)
                },
                imageVector = Icons.Filled.RepeatOne,
            )
        }
    }
}

@Composable
fun CircleImage(
    modifier: Modifier = Modifier,
    model: String,
) {
    AsyncImage(
        modifier =
            modifier
                .clip(shape = CircleShape)
                .border(
                    shape = CircleShape,
                    border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.primary),
                ),
        model = model,
        contentDescription = "",
    )
}

@Preview(name = "Dark")
@Composable
fun PlayingWithFavoriteSongBottomPlayerSheetPreview() {
    MusicPlayerTheme {
        FlexiblePlayerLayout(
            modifier = Modifier.height(820.dp),
            coverUri = "",
            isPlaying = true,
            isFavorite = true,
            title = "Song name",
            artist = "Artist name",
        )
    }
}

@Preview(name = "Dark")
@Composable
fun DarkBottomPlayerSheetPreview() {
    MusicPlayerTheme(darkTheme = true) {
        FlexiblePlayerLayout(
            modifier = Modifier.height(70.dp),
            coverUri = "",
            title = "Song name",
            artist = "Artist name",
        )
    }
}

@Preview(name = "Light")
@Composable
fun LightBottomPlayerSheetPreview() {
    MusicPlayerTheme(darkTheme = false) {
        FlexiblePlayerLayout(
            modifier = Modifier.height(70.dp),
            coverUri = "",
            title = "Song name",
            artist = "Artist name",
        )
    }
}

@Preview(name = "Light")
@Composable
fun LargeControlAreaPreview() {
    MusicPlayerTheme(darkTheme = false) {
        Surface {
            FadeInWithExpandArea(
                modifier = Modifier,
                title = "title",
                artist = "artist",
            )
        }
    }
}
