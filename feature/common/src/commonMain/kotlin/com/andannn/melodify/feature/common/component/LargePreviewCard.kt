package com.andannn.melodify.feature.common.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextOverflow
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.andannn.melodify.feature.common.theme.MelodifyTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LargePreviewCard(
    artCoverUri: String,
    title: String,
    subTitle: String,
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    placeholder: Painter? = null,
    error: Painter? = placeholder,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier.combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick,
        ),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column {
            AsyncImage(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .then(imageModifier),
                placeholder = placeholder,
                error = error,
                model = artCoverUri,
                contentDescription = "",
            )

            Spacer(modifier = Modifier.height(5.dp))

            Column(modifier = Modifier.padding(vertical = 3.dp, horizontal = 5.dp)) {
                Text(
                    text = title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = subTitle,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

        }
    }
}

@Preview
@Composable
private fun AlbumCardPreview() {
    MelodifyTheme {
        Surface {
            LargePreviewCard(
                imageModifier =
                Modifier
                    .clip(shape = CircleShape)
                    .alpha(0.3f)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)),
                placeholder = rememberVectorPainter(Icons.Rounded.Person),
                artCoverUri = "",
                title = "TitleTitleTitleTitleTitleTitleTitleTitleTitleTitleTitltleTitleTitleTitleTitleTitltleTitleTitleTitleTitleTitltleTitleTitleTitleTitleTitleTitleTitleTitleTitleTitleTitleTitleTitleTitleTitle",
                subTitle = "Sub title Sub title Sub title Sub title Sub title Sub title Sub title Sub title Sub title Sub title Sub title Sub title Sub title Sub title Sub title Sub title "
            )
        }
    }
}
