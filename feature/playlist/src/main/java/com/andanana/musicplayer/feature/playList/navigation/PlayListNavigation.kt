package com.andanana.musicplayer.feature.playList.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.andanana.musicplayer.feature.playList.PlayListScreen

private const val TAG = "PlayListNavigation"

const val MusicListRoute = "music_list_route"
const val MediaIdKey = "music_list_id"

fun NavController.navigateToPlayList(mediaId: String) {
    this.navigate("$MusicListRoute/$mediaId")
}

fun NavGraphBuilder.playListScreen(
    onShowMusicItemOption: (Uri) -> Unit,
) {
    composable(
        route = "$MusicListRoute/{$MediaIdKey}",
        arguments =
            listOf(
                navArgument(name = MediaIdKey) {
                    type = NavType.StringType
                },
            ),
    ) {
        PlayListScreen(
            onShowMusicItemOption = onShowMusicItemOption,
            onShowPlayListItemOption = onShowMusicItemOption,
        )
    }
}
