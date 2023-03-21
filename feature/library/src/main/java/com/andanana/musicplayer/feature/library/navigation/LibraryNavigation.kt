package com.andanana.musicplayer.feature.library.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.andanana.musicplayer.feature.library.LibraryRoute

const val libraryRoute = "library_route"

fun NavController.navigateToLibrary(navOptions: NavOptions? = null) {
    this.navigate(libraryRoute, navOptions)
}

fun NavGraphBuilder.libraryScreen(onNavigateToPlayScreen: (Uri) -> Unit) {
    composable(route = libraryRoute) {
        LibraryRoute(
            onNavigateToPlayScreen = onNavigateToPlayScreen
        )
    }
}
