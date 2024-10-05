package com.andannn.melodify.feature.playList.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.andannn.melodify.core.data.model.MediaListSource

const val MUSIC_LIST_ROUTE = "music_list_route"
const val SOURCE = "source"
const val ID = "album_id"

fun NavController.navigateToPlayList(id: String, source: MediaListSource) {
    this.navigate("$MUSIC_LIST_ROUTE/$source/$id")
}

fun NavGraphBuilder.playListScreen(onBackPressed: () -> Unit) {
    composable(
        route = "$MUSIC_LIST_ROUTE/{$SOURCE}/{$ID}",
        arguments =
            listOf(
                navArgument(name = ID) {
                    type = NavType.StringType
                },
                navArgument(name = SOURCE) {
                    type = NavType.EnumType(MediaListSource::class.java)
                },
            ),
    ) {
        PlayListScreen(onBackPressed = onBackPressed)
    }
}
