package com.andanana.musicplayer.core.designsystem.component

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andanana.musicplayer.core.data.util.loadImage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

private const val TAG = "MusicCard"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicCard(
    modifier: Modifier = Modifier,
    contentUri: Uri,
    title: String,
    artist: String,
    date: Int,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(10.dp).height(IntrinsicSize.Min)
        ) {
            val context = LocalContext.current.applicationContext
            val bitmapState by produceState<ImageResult>(initialValue = ImageResult.Loading) {
                try {
                    val bitmap = withContext(Dispatchers.IO) {
                        loadImage(
                            context,
                            contentUri
                        )
                    }

                    value = ImageResult.Success(bitmap)
                } catch (e: CancellationException) {
                    Log.d(TAG, "MusicCard: CancellationException $title")
                }
            }

            when (val state = bitmapState) {
                ImageResult.Loading -> {
                }
                is ImageResult.Success -> {
                    Image(
                        modifier = Modifier
                            .fillMaxHeight().width(50.dp)
                            .clip(MaterialTheme.shapes.extraSmall),
                        bitmap = state.bitmap.asImageBitmap(),
                        contentDescription = null
                    )
                }
            }

            Spacer(modifier = Modifier.width(3.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = artist,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM)
                            .format(date),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

sealed interface ImageResult {
    object Loading : ImageResult
    data class Success(val bitmap: Bitmap) : ImageResult
}

@Preview
@Composable
private fun MusicCardPreview() {
    MusicCard(
        contentUri = Uri.parse(""),
        title = "Title",
        artist = "artist",
        date = 0
    )
}
