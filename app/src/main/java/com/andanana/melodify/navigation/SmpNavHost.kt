package com.andanana.melodify.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.andanana.melodify.feature.home.navigation.HOME_ROUTE
import com.andanana.melodify.feature.home.navigation.homeScreen
import com.andanana.melodify.feature.playList.navigation.navigateToPlayList
import com.andanana.melodify.feature.playList.navigation.playListScreen

@Composable
fun SmpNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = HOME_ROUTE,
        modifier = modifier,
    ) {
        homeScreen(
            onNavigateToPlayList = { id, source ->
                navHostController.navigateToPlayList(id = id, source = source)
            },
        )
        playListScreen(
            onBackPressed = onBackPressed,
        )
    }
}
