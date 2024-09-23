package com.andannn.melodify.common.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.andannn.melodify.core.designsystem.icons.SmpIcon
import com.andannn.melodify.core.designsystem.theme.MelodifyTheme
import com.andannn.melodify.core.domain.model.AlbumItemModel
import com.andannn.melodify.core.domain.model.ArtistItemModel
import com.andannn.melodify.core.domain.model.AudioItemModel
import com.andannn.melodify.core.domain.model.MediaItemModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaBottomSheetView(
    bottomSheetModel: BottomSheetModel,
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope(),
    onDismissRequest: (SheetItem?) -> Unit = {},
) {
    val sheetState =
        rememberModalBottomSheetState()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            onDismissRequest.invoke(null)
        },
    ) {
        Surface(modifier = modifier.navigationBarsPadding()) {
            Column(Modifier.fillMaxWidth()) {
                SheetHeader(
                    mediaItem = bottomSheetModel.source,
                )

                HorizontalDivider()

                bottomSheetModel.bottomSheet.itemList.map { item ->
                    SheetItem(
                        item = item,
                        onClick = {
                            scope.launch {
                                sheetState.hide()
                                onDismissRequest.invoke(item)
                            }
                        },
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SheetHeader(
    mediaItem: MediaItemModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(vertical = 12.dp)
            .height(IntrinsicSize.Min)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier =
            Modifier
                .padding(start = 16.dp)
                .size(65.dp)
                .clip(MaterialTheme.shapes.extraSmall),
            model = mediaItem.artWorkUri,
            contentDescription = "",
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = mediaItem.name,
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(Modifier.weight(1f))

            Text(
                modifier = Modifier.alpha(0.7f),
                text = mediaItem.subTitle(),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

private fun MediaItemModel.subTitle() = when (this) {
    is AlbumItemModel -> {
        "$trackCount songs"
    }

    is ArtistItemModel -> {
        "$trackCount songs"
    }

    is AudioItemModel -> {
        artist
    }
}


@Composable
fun SheetItem(
    item: SheetItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier
            .clickable(onClick = onClick)
            .padding(16.dp)
            .fillMaxWidth(),
    ) {
        SmpIcon(item.smpIcon)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = item.text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun SmpIcon(item: SmpIcon) {
    when (item) {
        is SmpIcon.ImageVectorIcon -> {
            Icon(imageVector = item.imageVector, contentDescription = "")
        }
    }
}

@Preview
@Composable
private fun SheetHeaderPreview() {
    MelodifyTheme {
        Surface {
            SheetHeader(
                mediaItem = source,
            )
        }
    }
}

@Preview
@Composable
private fun MediaBottomDrawerDemo() {
    MelodifyTheme {
        var isShow by remember {
            mutableStateOf(false)
        }

        Surface {
            Button(onClick = { isShow = true }) {
                Text(text = "Show")
            }
        }

        if (isShow) {
            MediaBottomSheetView(
                bottomSheetModel = BottomSheetModel(
                    source = source,
                    bottomSheet = BottomSheet.MusicBottomSheet,
                ),
                onDismissRequest = {
                    isShow = false
                },
            )
        }
    }
}

private val source = AudioItemModel(
    id = 0,
    name = "Song 1",
    modifiedDate = 0,
    album = "Album 1",
    albumId = 0,
    artist = "Artist 1",
    artistId = 0,
    cdTrackNumber = 1,
    discNumberIndex = 0,
    artWorkUri = "",
)