package com.andanana.musicplayer.feature.home.navigation

import android.net.Uri
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.andanana.musicplayer.core.model.MusicInfo
import com.andanana.musicplayer.feature.home.HomeRoute

const val homeRoute = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(homeRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
    navHostController: NavHostController,
    onPlayMusicInList: (List<MusicInfo>, Int) -> Unit,
    onNavigateToPlayList: (Uri) -> Unit,
    onShowMusicItemOption: (Uri) -> Unit
) {
    composable(route = homeRoute) {
        val parentBackEntry = remember(it) {
            navHostController.getBackStackEntry(homeRoute)
        }
        HomeRoute(
            onPlayMusicInList = onPlayMusicInList,
            onNavigateToPlayList = onNavigateToPlayList,
            onShowMusicItemOption = onShowMusicItemOption
        )
    }
}
