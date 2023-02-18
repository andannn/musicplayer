package com.andanana.musicplayer.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.andanana.musicplayer.MainActivityViewModel
import com.andanana.musicplayer.core.model.MusicInfo
import com.andanana.musicplayer.feature.home.navigation.homeRoute
import com.andanana.musicplayer.feature.home.navigation.homeScreen
import com.andanana.musicplayer.feature.library.navigation.addPlayListDialog
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
    onPlayMusicInList: (List<MusicInfo>, Int) -> Unit
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No view model store owner"
    }
    val mainViewModel: MainActivityViewModel = remember(viewModelStoreOwner) {
        ViewModelProvider(viewModelStoreOwner)[MainActivityViewModel::class.java]
    }
    val interactingMusic by mainViewModel.interactingMusicItem.collectAsState(null)
    NavHost(
        navController = navHostController,
        startDestination = homeRoute,
        modifier = modifier
    ) {
        homeScreen(
            onNavigateToPlayList = {
                navHostController.navigateToPlayList(it)
            },
            onShowMusicItemOption = onShowMusicItemOption,
            onPlayMusicInList = onPlayMusicInList
        )
        libraryScreen()
        playListScreen(
            interactingMusic = interactingMusic,
            onShowMusicItemOption = onShowMusicItemOption,
            onPlayMusicInList = onPlayMusicInList,
            onBackPressed = onBackPressed
        )
        playerScreen(
            navHostController = navHostController
        )
        addPlayListDialog()
    }
}
