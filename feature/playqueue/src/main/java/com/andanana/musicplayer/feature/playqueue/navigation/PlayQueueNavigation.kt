package com.andanana.musicplayer.feature.playqueue.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

private const val TAG = "PlayListNavigation"

const val playingQueueRoute = "play_list_route"

fun NavController.navigateToPlayQueue() {
    this.navigate(playingQueueRoute)
}

fun NavGraphBuilder.playQueueScreen(onBackPressed: () -> Unit) {
    composable(
        route = playingQueueRoute,
    ) {
//        PlayQueueScreen()
    }
}
