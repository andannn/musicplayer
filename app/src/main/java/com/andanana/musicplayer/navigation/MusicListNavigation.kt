package com.andanana.musicplayer.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val musicListNavigation = "music_list_navigation"

fun NavGraphBuilder.musicListScreen() {
    composable(route = musicListNavigation) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Red))
    }
}
