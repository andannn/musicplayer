package com.andanana.musicplayer.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.andanana.musicplayer.core.domain.model.MediaListSource
import com.andanana.musicplayer.feature.home.HomeRoute

const val HOME_ROUTE = "home_route"

fun NavGraphBuilder.homeScreen(
    onNavigateToPlayList: (id: String, source: MediaListSource) -> Unit,
) {
    composable(route = HOME_ROUTE) {
        HomeRoute(
            onNavigateToPlayList = onNavigateToPlayList,
        )
    }
}
