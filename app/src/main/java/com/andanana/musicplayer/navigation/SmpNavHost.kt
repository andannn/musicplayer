package com.andanana.musicplayer.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.dialog
import com.andanana.musicplayer.feature.home.navigation.homeRoute
import com.andanana.musicplayer.feature.home.navigation.homeScreen
import com.andanana.musicplayer.feature.library.navigation.libraryScreen
import com.andanana.musicplayer.feature.playList.navigation.navigateToPlayList
import com.andanana.musicplayer.feature.playList.navigation.playListScreen
import com.andanana.musicplayer.feature.player.navigation.playerScreen

private const val TAG = "SmpNavHost"

@Composable
fun SmpNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    onShowMusicItemOption: (Uri) -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = homeRoute,
        modifier = modifier
    ) {
        homeScreen(
            navHostController = navHostController,
            onNavigateToPlayList = {
                navHostController.navigateToPlayList(it)
            },
            onShowMusicItemOption = onShowMusicItemOption
        )
        libraryScreen()
        playListScreen(
            navHostController = navHostController,
            onBackPressed = onBackPressed,
            onShowMusicItemOption = onShowMusicItemOption
        )
        playerScreen(
            navHostController = navHostController
        )
    }
}
