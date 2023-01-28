package com.andanana.musicplayer.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.andanana.musicplayer.feature.home.navigation.homeRoute
import com.andanana.musicplayer.feature.home.navigation.homeScreen
import com.andanana.musicplayer.feature.library.navigation.libraryScreen

private const val TAG = "SmpNavHost"

@Composable
fun SmpNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    val a by navHostController.visibleEntries.collectAsState()
    Log.d(TAG, "SmpNavHost: ________________________")
    a.forEach {
        Log.d(TAG, "SmpNavHost: ${it.destination}")
    }
    NavHost(
        navController = navHostController,
        startDestination = homeRoute,
        modifier = modifier
    ) {
        homeScreen()
        libraryScreen()
    }
}
