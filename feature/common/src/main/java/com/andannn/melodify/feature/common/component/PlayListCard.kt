package com.andannn.melodify.feature.common.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun PlayListCard(
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.cardColors(),
    albumArtUri: String = "",
    coverImage: ImageVector? = null,
    title: String = "",
    trackCount: Int = 0,
    onPlayListItemClick: () -> Unit = {},
    onOptionButtonClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = colors,
        shape = MaterialTheme.shapes.medium,
        onClick = onPlayListItemClick
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(50.dp)) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.extraSmall),
                    model = albumArtUri,
                    contentDescription = ""
                )
                coverImage?.let {
                    Image(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.2f),
                                shape = RoundedCornerShape(5.dp)
                            )
                            .padding(4.dp)
                            .fillMaxSize(),
                        imageVector = coverImage,
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
                    text = "$trackCount songs",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(
                modifier = Modifier,
                onClick = onOptionButtonClick
            ) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "menu")
            }
        }
    }
}

@Preview
@Composable
private fun PlayListCardPreview() {
    PlayListCard(
        albumArtUri = "",
        title = "Title",
        coverImage = Icons.Rounded.FavoriteBorder,
        trackCount = 0
    )
}
