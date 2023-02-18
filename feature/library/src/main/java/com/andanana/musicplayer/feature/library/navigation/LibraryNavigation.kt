package com.andanana.musicplayer.feature.library.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val libraryRoute = "library_route"

fun NavController.navigateToLibrary(navOptions: NavOptions? = null) {
    this.navigate(libraryRoute, navOptions)
}

fun NavGraphBuilder.libraryScreen() {
    composable(route = libraryRoute) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Blue))
    }
}
