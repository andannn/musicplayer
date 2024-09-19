package com.andanana.musicplayer.feature.playList.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.andanana.musicplayer.feature.playList.PlayListScreen

const val MUSIC_LIST_ROUTE = "music_list_route"
const val ID = "album_id"

fun NavController.navigateToPlayList(id: String) {
    this.navigate("$MUSIC_LIST_ROUTE/$id")
}

fun NavGraphBuilder.playListScreen(onBackPressed: () -> Unit) {
    composable(
        route = "$MUSIC_LIST_ROUTE/{$ID}",
        arguments =
            listOf(
                navArgument(name = ID) {
                    type = NavType.StringType
                },
            ),
    ) {
        PlayListScreen(onBackPressed = onBackPressed)
    }
}
