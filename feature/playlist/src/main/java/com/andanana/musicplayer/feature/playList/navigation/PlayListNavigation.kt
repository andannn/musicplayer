package com.andanana.musicplayer.feature.playList.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.andanana.musicplayer.feature.playList.PlayListScreen

const val MusicListRoute = "music_list_route"
const val MediaIdKey = "music_list_id"

fun NavController.navigateToPlayList(mediaId: String) {
    this.navigate("$MusicListRoute/$mediaId")
}

fun NavGraphBuilder.playListScreen() {
    composable(
        route = "$MusicListRoute/{$MediaIdKey}",
        arguments =
            listOf(
                navArgument(name = MediaIdKey) {
                    type = NavType.StringType
                },
            ),
    ) {
        PlayListScreen()
    }
}
