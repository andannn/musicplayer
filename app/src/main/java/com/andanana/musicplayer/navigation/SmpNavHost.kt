package com.andanana.musicplayer.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.andanana.musicplayer.feature.home.navigation.HOME_ROUTE
import com.andanana.musicplayer.feature.home.navigation.homeScreen
import com.andanana.musicplayer.feature.playqueue.navigation.playQueueScreen

private const val TAG = "SmpNavHost"

@Composable
fun SmpNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    onShowMusicItemOption: (Uri) -> Unit,
    onNewPlayListButtonClick: () -> Unit,
    onCreateButtonClick: (name: String) -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = HOME_ROUTE,
        modifier = modifier,
    ) {
        homeScreen(
            onNavigateToPlayList = { mediaId ->
//                navHostController.navigateToPlayList(mediaId = mediaId)
            },
            onShowMusicItemOption = onShowMusicItemOption,
        )
        playQueueScreen(
            onBackPressed = onBackPressed,
        )
    }
}
