package com.andanana.musicplayer.feature.playList.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.andanana.musicplayer.feature.playList.PlayListScreen

val requestUriArg = "request_play_list_uri"

fun NavController.navigateToPlayList(uri: Uri) {
    this.navigate("play_list_route/$uri")
}

fun NavGraphBuilder.playListScreen(
    onBackPressed: () -> Unit
) {
    composable(
        route = "play_list_route/{$requestUriArg}",
        arguments = listOf(
            navArgument(name = requestUriArg) {
                type = NavType.StringType
            }
        )
    ) {
        PlayListScreen()
    }
}