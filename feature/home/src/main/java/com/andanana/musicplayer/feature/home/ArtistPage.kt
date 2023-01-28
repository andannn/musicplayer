package com.andanana.musicplayer.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ArtistPage(
    modifier: Modifier = Modifier,
    artistPageViewModel: ArtistPageViewModel = hiltViewModel()
) {
    Box(modifier = modifier.background(Color.Black))
}
