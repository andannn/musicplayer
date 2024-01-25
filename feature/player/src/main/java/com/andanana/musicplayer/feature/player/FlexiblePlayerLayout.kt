package com.andanana.musicplayer.feature.player

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import coil.compose.AsyncImage
import com.andanana.musicplayer.core.designsystem.R
import com.andanana.musicplayer.core.designsystem.theme.MusicPlayerTheme

private const val TAG = "BottomPlayerSheet"

val MaxExpandPadding = 20.dp
val MinImageSize = 60.dp
val MinImagePaddingTop = 5.dp
val MaxImagePaddingTop = 100.dp

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
    onPlayerSheetClick: () -> Unit = {},
    onPlayControlButtonClick: () -> Unit = {},
    onPlayNextButtonClick: () -> Unit = {},
    onFavoriteButtonClick: () -> Unit = {},
) {
    val statusBarHeight =
        with(LocalDensity.current) {
            WindowInsets.statusBars.getTop(this).toDp()
        }

    Surface(
        modifier =
            modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        shadowElevation = 10.dp,
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
        ) {
            val maxImageWidth = maxWidth - MaxExpandPadding * 2
            val minImageWidth = MinImageSize

            val (minHeightPx, maxHeightPx) = heightPxRange.start to heightPxRange.endInclusive
            val currentHeight = constraints.maxHeight
            val expandFactor =
                (currentHeight - minHeightPx).div(maxHeightPx - minHeightPx).coerceIn(0f, 1f)
            Log.d(TAG, "FlexiblePlayerLayout: expandFactor $expandFactor")

            val imageWidthDp = lerp(start = minImageWidth, stop = maxImageWidth, expandFactor)
            val imagePaddingTopDp =
                lerp(start = MinImagePaddingTop, stop = MaxImagePaddingTop, expandFactor)

            val fadingAreaPaddingTop =
                lerp(
                    start = MinFadeoutWithExpandAreaPaddingTop,
                    stop = statusBarHeight,
                    expandFactor,
                )
            CircleImage(
                modifier =
                    Modifier
                        .align(Alignment.TopStart)
                        .padding(top = imagePaddingTopDp)
                        .padding(start = MaxExpandPadding)
                        .width(imageWidthDp)
                        .aspectRatio(1f),
                model = coverUri,
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
            ) {
                FadeoutWithExpandArea(
                    modifier =
                        Modifier.padding(
                            top = fadingAreaPaddingTop,
                            start = MaxExpandPadding + MinImageSize,
                        ),
                    title = title,
                    artist = artist,
                    isPlaying = isPlaying,
                    isFavorite = isFavorite,
                    onPlayControlButtonClick = onPlayControlButtonClick,
                    onPlayNextButtonClick = onPlayNextButtonClick,
                    onFavoriteButtonClick = onFavoriteButtonClick,
                )
            }

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

@Composable
private fun FadeoutWithExpandArea(
    modifier: Modifier = Modifier,
    title: String,
    artist: String,
    isPlaying: Boolean,
    isFavorite: Boolean,
    onPlayControlButtonClick: () -> Unit,
    onPlayNextButtonClick: () -> Unit,
    onFavoriteButtonClick: () -> Unit,
) {
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
                text = title,
                maxLines = 1,
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = artist,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        IconButton(
            modifier =
                Modifier
                    .size(30.dp)
                    .scale(1.2f),
            onClick = onPlayControlButtonClick,
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
            onClick = onPlayNextButtonClick,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.music_music_player_player_previous_icon),
                contentDescription = "",
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        IconButton(
            modifier = Modifier.size(30.dp),
            onClick = onFavoriteButtonClick,
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
            modifier = Modifier.height(520.dp),
            coverUri = "",
            title = "Song name",
            artist = "Artist name",
            isFavorite = true,
            isPlaying = true,
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
