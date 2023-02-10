package com.andanana.musicplayer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.andanana.musicplayer.feature.home.navigation.homeRoute
import com.andanana.musicplayer.feature.home.navigation.homeScreen
import com.andanana.musicplayer.feature.library.navigation.libraryScreen
import com.andanana.musicplayer.feature.playList.navigation.navigateToPlayList
import com.andanana.musicplayer.feature.playList.navigation.playListScreen

private const val TAG = "SmpNavHost"

@Composable
fun SmpNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    onGetRootViewModelStoreOwner: () -> ViewModelStoreOwner,
    onBackPressed: () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = homeRoute,
        modifier = modifier
    ) {
        homeScreen(
            onGetRootViewModelStoreOwner = onGetRootViewModelStoreOwner,
            onNavigateToPlayList = {
                navHostController.navigateToPlayList(it)
            }
        )
        libraryScreen()
        playListScreen(onBackPressed = onBackPressed)
    }
}
