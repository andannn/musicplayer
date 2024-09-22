package com.andannn.melodify.feature.player.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.andannn.melodify.core.domain.model.AudioItemModel
import com.andannn.melodify.core.designsystem.component.CircleBorderImage
import com.andannn.melodify.core.designsystem.theme.MusicPlayerTheme
import com.andannn.melodify.core.designsystem.util.verticalGradientScrim
import com.andannn.melodify.core.domain.model.PlayMode
import com.andannn.melodify.feature.player.PlayerUiEvent

val MinImageSize = 60.dp
// MaxImageSize is calculated.

val MinImagePaddingTop = 5.dp
val MaxImagePaddingTop = 130.dp

val MinImagePaddingStart = 5.dp
val MaxImagePaddingStart = 20.dp

val MinFadeoutWithExpandAreaPaddingTop = 15.dp

val BottomSheetDragAreaHeight = 90.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlexiblePlayerLayout(
    layoutState: PlayerLayoutState,
    coverUri: String,
    activeMediaItem: AudioItemModel,
    playListQueue: List<AudioItemModel>,
    modifier: Modifier = Modifier,
    playMode: PlayMode = PlayMode.REPEAT_ALL,
    isShuffle: Boolean = false,
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
        shadowElevation = 10.dp,
    ) {
        val primaryColor = MaterialTheme.colorScheme.primary
        val backGroundModifier =
            if (layoutState.isPlayerExpanding) {
                Modifier.verticalGradientScrim(
                    color = primaryColor.copy(alpha = 0.38f),
                    startYPercentage = 1f,
                    endYPercentage = 0f,
                )
            } else {
                Modifier
            }

        BoxWithConstraints(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                    .then(backGroundModifier),
        ) {
            val playerExpandFactor = layoutState.playerExpandFactor
            val isLayoutFullyExpand = playerExpandFactor == 1f

            val sheetShrinkFactor = layoutState.sheetShrinkFactor
            val isSheetExpanding = layoutState.isSheetExpanding

            val factor =
                remember(isSheetExpanding, playerExpandFactor, sheetShrinkFactor) {
                    if (isSheetExpanding) sheetShrinkFactor else playerExpandFactor
                }

            // states to control ui.
            val maxImageWidth = maxWidth - MaxImagePaddingStart * 2
            val imageWidthDp = lerp(start = MinImageSize, stop = maxImageWidth, factor)
            val imagePaddingTopDp =
                lerp(
                    start = MinImagePaddingTop + if (isSheetExpanding) statusBarHeight else 0.dp,
                    stop = MaxImagePaddingTop,
                    factor,
                )
            val imagePaddingStartDp =
                lerp(start = MinImagePaddingStart, stop = MaxImagePaddingStart, factor)
            val fadingAreaPaddingTop =
                lerp(
                    start = MinFadeoutWithExpandAreaPaddingTop + if (isSheetExpanding) statusBarHeight else 0.dp,
                    stop = statusBarHeight,
                    factor,
                )
            val fadeInAreaAlpha = (1f - (1f - factor).times(3f)).coerceIn(0f, 1f)
            val fadeoutAreaAlpha = 1 - (factor * 4).coerceIn(0f, 1f)

            MiniPlayerLayout(
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
                enabled = fadeoutAreaAlpha == 1f,
                title = title,
                artist = artist,
                isPlaying = isPlaying,
                isFavorite = isFavorite,
                onEvent = onEvent,
            )

            IconButton(
                modifier =
                    Modifier
                        .padding(top = statusBarHeight, start = 4.dp)
                        .rotate(-90f)
                        .graphicsLayer {
                            alpha = fadeInAreaAlpha
                        },
                enabled = fadeInAreaAlpha == 1f,
                onClick = onShrinkButtonClick,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                    contentDescription = "Shrink",
                )
            }
            IconButton(
                modifier =
                    Modifier
                        .padding(top = statusBarHeight, end = 4.dp)
                        .align(Alignment.TopEnd)
                        .graphicsLayer {
                            alpha = fadeInAreaAlpha
                        },
                enabled = fadeInAreaAlpha == 1f,
                onClick = {
                    onEvent(PlayerUiEvent.OnOptionIconClick(activeMediaItem))
                },
            ) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Menu")
            }

            CircleBorderImage(
                modifier =
                    Modifier
                        .padding(top = imagePaddingTopDp)
                        .padding(start = imagePaddingStartDp)
                        .width(imageWidthDp)
                        .aspectRatio(1f),
                model = coverUriState.value,
            )

            Column {
                LargePlayerControlArea(
                    modifier =
                        Modifier
                            .padding(top = imagePaddingTopDp)
                            .padding(top = imageWidthDp)
                            .weight(1f)
                            .graphicsLayer {
                                alpha = fadeInAreaAlpha
                            }
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max),
                    enabled = fadeInAreaAlpha >= 0.7f,
                    isPlaying = isPlaying,
                    playMode = playMode,
                    isShuffle = isShuffle,
                    progress = progress,
                    title = title,
                    artist = artist,
                    onEvent = onEvent,
                )
                if (isLayoutFullyExpand) {
                    Spacer(modifier = Modifier.height(BottomSheetDragAreaHeight))
                }
            }

            AnimatedVisibility(
                modifier =
                    Modifier.align(Alignment.BottomCenter),
                enter = fadeIn(),
                exit = fadeOut(),
                visible = isLayoutFullyExpand,
            ) {
                BottomPlayQueueSheet(
                    sheetMaxHeightDp = with(LocalDensity.current) { layoutState.sheetHeight.toDp() },
                    state = layoutState.sheetState,
                    activeMediaItem = activeMediaItem,
                    playListQueue = playListQueue,
                    onEvent = onEvent,
                )
            }

            if (!layoutState.isPlayerExpanding) {
                Spacer(
                    modifier =
                        Modifier
                            .fillMaxWidth(fraction = progress)
                            .align(Alignment.BottomStart)
                            .padding(bottom = with(LocalDensity.current) { layoutState.navigationBarHeight.toDp() })
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

@Preview
@Composable
private fun FlexiblePlayerLayoutExpandPreview() {
    MusicPlayerTheme {
        val layoutState =
            PlayerLayoutState(
                screenHeight = 2300,
                navigationBarHeight = 78,
                density = LocalDensity.current,
                animaScope = rememberCoroutineScope(),
                statusBarHeight = 83,
            )
        FlexiblePlayerLayout(
            layoutState = layoutState,
            coverUri = "",
            modifier = Modifier.height(870.dp),
            isPlaying = true,
            isFavorite = true,
            title = "Song name",
            artist = "Artist name",
            playListQueue = emptyList(),
            activeMediaItem = AudioItemModel.DEFAULT
        )
    }
}

@Preview
@Composable
private fun FlexiblePlayerLayoutShrinkPreview() {
    MusicPlayerTheme(darkTheme = false) {
        val layoutState =
            PlayerLayoutState(
                screenHeight = 2300,
                navigationBarHeight = 78,
                density = LocalDensity.current,
                animaScope = rememberCoroutineScope(),
                statusBarHeight = 83,
            )
        FlexiblePlayerLayout(
            layoutState = layoutState,
            coverUri = "",
            modifier = Modifier.height(70.dp),
            title = "Song name",
            artist = "Artist name",
            playListQueue = emptyList(),
            activeMediaItem = AudioItemModel.DEFAULT
        )
    }
}
