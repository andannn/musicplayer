package com.andanana.musicplayer.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
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
        @Composable get() = navController.currentDestination

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {

    }
}
