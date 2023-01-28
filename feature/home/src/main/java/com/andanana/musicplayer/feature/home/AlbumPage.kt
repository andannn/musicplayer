package com.andanana.musicplayer.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel

private const val TAG = "AlbumPage"

@Composable
fun AlbumPage(
    modifier: Modifier = Modifier,
    albumPageViewModel: AlbumPageViewModel = hiltViewModel()
) {

    Box(modifier = modifier.background(Color.Blue))
}
