package com.andannn.melodify.feature.player.lyrics

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andannn.melodify.core.designsystem.theme.MelodifyTheme
import com.andannn.melodify.core.domain.LyricModel

@Composable
internal fun LyricsView(
    lyricModel: LyricModel,
    modifier: Modifier = Modifier,
) {
    Text(text = lyricModel.toString())
}

@Preview
@Composable
private fun LyricsViewPreview() {
    MelodifyTheme {
        Surface {
            LyricsView(lyricModel = LyricModel(
                plainLyrics = "AAAAAAAA",
                syncedLyrics = "AAAAAAAAAAAa"
            ))
        }
    }
}