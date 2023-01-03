package com.andanana.musicplayer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.andanana.musicplayer.feature.musiclist.navigation.musicListNavigation
import com.andanana.musicplayer.feature.musiclist.navigation.musicListScreen

@Composable
fun SimpleMusicNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navHostController,
        startDestination = musicListNavigation,
        modifier = modifier
    ) {
        musicListScreen()
    }
}