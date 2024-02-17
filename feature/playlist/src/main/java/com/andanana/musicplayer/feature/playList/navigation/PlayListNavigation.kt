package com.andanana.musicplayer.feature.playList.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.andanana.musicplayer.feature.playList.PlayListScreen

const val MUSIC_LIST_ROUTE = "music_list_route"
const val MEDIA_ID = "music_list_id"

fun NavController.navigateToPlayList(mediaId: String) {
    this.navigate("$MUSIC_LIST_ROUTE/$mediaId")
}

fun NavGraphBuilder.playListScreen(onBackPressed: () -> Unit) {
    composable(
        route = "$MUSIC_LIST_ROUTE/{$MEDIA_ID}",
        arguments =
            listOf(
                navArgument(name = MEDIA_ID) {
                    type = NavType.StringType
                },
            ),
    ) {
        PlayListScreen(onBackPressed = onBackPressed)
    }
}
