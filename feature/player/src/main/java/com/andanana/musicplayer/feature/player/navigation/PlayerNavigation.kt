package com.andanana.musicplayer.feature.player.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.andanana.musicplayer.feature.player.PlayerScreen

const val playerRoute = "player_route"

fun NavHostController.navigateToPlayer() {
    this.navigate(route = playerRoute)
}

fun NavGraphBuilder.playerScreen(
    navHostController: NavHostController
) {
    composable(
        route = playerRoute
    ) {
        val parentBackEntry = remember(it) {
            navHostController.getBackStackEntry("home_route")
        }
        PlayerScreen(
            playerStateViewModel = hiltViewModel(parentBackEntry)
        )
    }
}
