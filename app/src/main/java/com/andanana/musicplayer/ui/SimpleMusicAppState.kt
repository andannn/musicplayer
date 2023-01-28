package com.andanana.musicplayer.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.andanana.musicplayer.feature.home.navigation.homeRoute
import com.andanana.musicplayer.feature.home.navigation.navigateToHome
import com.andanana.musicplayer.feature.library.navigation.libraryRoute
import com.andanana.musicplayer.feature.library.navigation.navigateToLibrary
import com.andanana.musicplayer.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberSimpleMusicAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController()
): SimpleMusicAppState {
    return remember(navController, coroutineScope) {
        SimpleMusicAppState(navController, coroutineScope)
    }
}

class SimpleMusicAppState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope
) {
    val topLevelDestinations = TopLevelDestination.values().toList()

    val currentNavDestination
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination
        @Composable get() = when (currentNavDestination?.route) {
            homeRoute -> TopLevelDestination.HOME
            libraryRoute -> TopLevelDestination.LIBRARY
            else -> null
        }

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
}
