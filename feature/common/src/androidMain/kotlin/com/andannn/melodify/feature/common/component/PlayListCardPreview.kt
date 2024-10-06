package com.andannn.melodify.feature.common.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

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