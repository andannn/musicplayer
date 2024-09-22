package com.andanana.melodify.core.designsystem.component

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Card
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.andanana.melodify.core.designsystem.theme.MusicPlayerTheme

@Composable
fun LargePreviewCard(
    artCoverUri: Uri,
    title: String,
    trackCount: Int,
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    placeholder: Painter? = null,
    error: Painter? = placeholder,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        onClick = onClick,
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
                    text = "$trackCount Tracks",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

        }
    }
}

@Preview
@Composable
private fun AlbumCardPreview() {
    MusicPlayerTheme {
        Surface {
            LargePreviewCard(
                imageModifier =
                Modifier
                    .clip(shape = CircleShape)
                    .alpha(0.3f)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)),
                placeholder = rememberVectorPainter(Icons.Rounded.Person),
                artCoverUri = Uri.parse(""),
                title = "TitleTitleTitleTitleTitleTitleTitleTitleTitleTitleTitltleTitleTitleTitleTitleTitltleTitleTitleTitleTitleTitltleTitleTitleTitleTitleTitleTitleTitleTitleTitleTitleTitleTitleTitleTitleTitle",
                trackCount = 3,
            )
        }
    }
}
