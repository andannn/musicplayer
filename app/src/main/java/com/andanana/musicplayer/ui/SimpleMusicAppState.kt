package com.andanana.musicplayer.ui

import android.net.Uri
import androidx.compose.material.BottomDrawerState
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.andanana.musicplayer.core.designsystem.Drawer
import com.andanana.musicplayer.core.model.RequestType
import com.andanana.musicplayer.core.model.RequestType.Companion.toRequestType
import com.andanana.musicplayer.core.model.toDrawer
import com.andanana.musicplayer.feature.home.navigation.homeRoute
import com.andanana.musicplayer.feature.home.navigation.navigateToHome
import com.andanana.musicplayer.feature.library.navigation.libraryRoute
import com.andanana.musicplayer.feature.library.navigation.navigateToLibrary
import com.andanana.musicplayer.feature.playList.navigation.playListRoute
import com.andanana.musicplayer.feature.player.navigation.playerRoute
import com.andanana.musicplayer.navigation.TopLevelDestination
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val TAG = "SimpleMusicAppState"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberSimpleMusicAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    drawerState: BottomDrawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed),
    navController: NavHostController = rememberNavController(),
    systemUiController: SystemUiController = rememberSystemUiController()
): SimpleMusicAppState {
    return remember(
        navController,
        coroutineScope,
        systemUiController,
        drawerState,
        snackbarHostState
    ) {
        SimpleMusicAppState(
            navController,
            coroutineScope,
            systemUiController,
            drawerState,
            snackbarHostState
        )
    }
}

class SimpleMusicAppState @OptIn(ExperimentalMaterialApi::class) constructor(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val systemUiController: SystemUiController,
    val drawerState: BottomDrawerState,
    val snackbarHostState: SnackbarHostState
) {
    val topLevelDestinations = TopLevelDestination.values().toList()

    val currentBackStackEntry
        @Composable get() = navController.currentBackStackEntryAsState()

    val currentNavDestination
        @Composable get() = currentBackStackEntry.value?.destination

    private val currentDestinationWithoutDialog
        get() = navController.backQueue.lastOrNull {
            it.destination.route?.contains("dialog") == false
        }?.destination

    val isHomeRoute
        get() = currentDestinationWithoutDialog?.route == homeRoute

    val isLibraryRoute
        get() = currentDestinationWithoutDialog?.route == libraryRoute

    val isPlayerRoute
        get() = currentDestinationWithoutDialog?.route == playerRoute

    private val isPlayListRoute
        get() = currentDestinationWithoutDialog?.route?.contains(playListRoute) == true

    val isTopBarHide
        get() = (isPlayListRoute) || isPlayerRoute

    val isNavigationBarHide
        get() = isPlayListRoute || isPlayerRoute

    val currentTopLevelDestination
        @Composable get() = when (currentNavDestination?.route) {
            homeRoute -> TopLevelDestination.HOME
            libraryRoute -> TopLevelDestination.LIBRARY
            else -> null
        }

    val drawer: MutableState<Drawer?> = mutableStateOf(null)

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.HOME -> navController.navigateToHome(topLevelNavOptions)
            TopLevelDestination.LIBRARY -> navController.navigateToLibrary(topLevelNavOptions)
            else -> {}
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    fun showDrawerByUri(uri: Uri) {
        drawer.value = when (val type = uri.toRequestType()) {
            RequestType.MUSIC_REQUEST,
            RequestType.ALBUM_REQUEST,
            RequestType.ARTIST_REQUEST -> type.toDrawer()
            else -> error("Invalid Type")
        }
        coroutineScope.launch {
            drawerState.open()
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    fun closeDrawer() {
        coroutineScope.launch {
            drawerState.close()
        }
    }

    fun onBackPressed() {
        navController.popBackStack()
    }
}
