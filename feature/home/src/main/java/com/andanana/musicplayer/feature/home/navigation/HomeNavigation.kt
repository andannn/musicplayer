package com.andanana.musicplayer.feature.home.navigation

import android.net.Uri
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.andanana.musicplayer.feature.home.HomeRoute

const val homeRoute = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(homeRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onGetRootViewModelStoreOwner: () -> ViewModelStoreOwner,
    onNavigateToPlayList: (Uri) -> Unit
) {
    composable(route = homeRoute) {
        HomeRoute(
            onGetRootViewModelStoreOwner = onGetRootViewModelStoreOwner,
            onNavigateToPlayList = onNavigateToPlayList
        )
    }
}
