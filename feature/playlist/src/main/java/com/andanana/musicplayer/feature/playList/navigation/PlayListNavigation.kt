package com.andanana.musicplayer.feature.playList.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.andanana.musicplayer.core.data.model.MusicListType
import com.andanana.musicplayer.feature.playList.PlayListScreen

private const val TAG = "PlayListNavigation"

const val MusicListRoute = "music_list_route"
const val MusicListTypeKey = "music_list_type"
const val MusicListIdKey = "music_list_id"

fun NavController.navigateToPlayList(musicListId: Long, musicListType: MusicListType) {
        this.navigate("$MusicListRoute/$musicListId/$musicListType")
}

fun NavGraphBuilder.playListScreen(
    onShowMusicItemOption: (Uri) -> Unit,
    onBackPressed: () -> Unit
) {
    composable(
        route = "$MusicListRoute/{$MusicListIdKey}/{$MusicListTypeKey}",
        arguments = listOf(
            navArgument(name = MusicListIdKey) {
                type = NavType.LongType
            },
            navArgument(name = MusicListTypeKey) {
                type = NavType.EnumType(MusicListType::class.java)
            }
        )
    ) {
        PlayListScreen(
            onShowMusicItemOption = onShowMusicItemOption,
            onShowPlayListItemOption = onShowMusicItemOption
        )
    }
}
