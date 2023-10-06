package com.andanana.musicplayer.feature.home.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.andanana.musicplayer.core.data.model.MusicListType
import com.andanana.musicplayer.feature.home.HomeRoute

const val homeRoute = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(homeRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onNavigateToPlayList: (mediaId: String) -> Unit,
    onShowMusicItemOption: (Uri) -> Unit
) {
    composable(route = homeRoute) {
        HomeRoute(
            onNavigateToPlayList = onNavigateToPlayList,
            onShowMusicItemOption = onShowMusicItemOption
        )
    }
}
