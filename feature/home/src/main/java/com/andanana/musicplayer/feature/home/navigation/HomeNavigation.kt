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
    onNavigateToPlayList: (Uri) -> Unit,
    onShowMusicItemOption: (Uri) -> Unit
) {
    composable(route = homeRoute) {
        HomeRoute(
            onNavigateToPlayList = onNavigateToPlayList,
            onShowMusicItemOption = onShowMusicItemOption
        )
    }
}
